package com.casecode.core.data.repository

import android.app.Activity
import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.casecode.core.common.network.Dispatcher
import com.casecode.core.common.network.GeoAlertDispatchersDispatchers.IO
import com.casecode.core.common.result.Resource
import com.casecode.core.common.result.Result
import com.casecode.core.data.R
import com.casecode.core.data.model.toUser
import com.google.android.gms.common.api.UnsupportedApiCallException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class AuthServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val googleIdOption: GetGoogleIdOption,
    private val userRepository: UserRepository,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : AuthService {

    private val credentialManager: CredentialManager = CredentialManager.create(context)

    override val currentUserId: String
        get() = firebaseAuth.currentUser?.uid.orEmpty()

    override val currentUser: Flow<FirebaseUser?>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth -> this.trySend(auth.currentUser) }
            firebaseAuth.addAuthStateListener(listener)
            awaitClose { firebaseAuth.removeAuthStateListener(listener) }
        }

    override val hasUser: Boolean
        get() = firebaseAuth.currentUser?.uid?.isNotBlank() == true

    override suspend fun signIn(activity: Activity): Resource<Int> {
        return trace(SIGN_IN) {
            withContext(ioDispatcher) {
                try {
                    val googleIdToken = retrieveGoogleIdToken(activity)
                    val googleCredentials = buildGoogleAuthCredential(googleIdToken)
                    val authResult = signInWithGoogleCredentials(googleCredentials)

                    authResult.user?.let { user ->
                        val isNewUser = authResult.additionalUserInfo?.isNewUser == true
                        if (isNewUser) {
                            val userData = user.toUser()
                            when (userRepository.addUser(userData)) {
                                is Result.Success -> Resource.success(R.string.sign_in_success_new_user)
                                is Result.Error -> Resource.error(R.string.sign_in_failure)
                                else -> Resource.error(R.string.sign_in_failure)
                            }
                        } else {
                            Resource.success(R.string.sign_in_success)
                        }
                    } ?: Resource.empty(null, R.string.sign_in_failure)
                } catch (e: GetCredentialException) {
                    Resource.error(R.string.sign_in_exception)
                } catch (e: UnsupportedApiCallException) {
                    Resource.error(R.string.unsupported_api_call)
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    Resource.error(R.string.invalid_credentials)
                } catch (e: FirebaseAuthException) {
                    Resource.error(R.string.sign_in_api_exception)
                } catch (e: Exception) {
                    Resource.error(R.string.sign_in_failure)
                }
            }
        }
    }

    private suspend fun signInWithGoogleCredentials(credentials: AuthCredential): AuthResult {
        return firebaseAuth.signInWithCredential(credentials).await()
    }

    override suspend fun checkRegistration(email: String): Resource<Boolean> {
        return withContext(ioDispatcher) {
            try {
                // Create a temporary user with a generic password
                firebaseAuth.createUserWithEmailAndPassword(email, "temporary_password")
                // Account creation succeeded, email is available
                Timber.e("checkRegistration: email is created before :true")
                Resource.Success(true)
            } catch (e: FirebaseAuthUserCollisionException) {
                Timber.e("checkRegistration: email is created before :false")
                // Email already exists
                Resource.Success(false) // Assuming password-based sign-in
            } catch (e: Exception) {
                // Other errors
                Resource.Error(e.message)
            }
        }
    }

    override suspend fun isSignIn(): Resource<Boolean> {
        return withContext(ioDispatcher) {
            try {
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    Resource.success(true)
                } else {
                    Resource.empty(null, false)
                }
            } catch (e: Exception) {
                Resource.error(e.message)
            }
        }
    }

    override suspend fun signOut(): Flow<Result<Unit>> = flow {
        trace(SIGN_OUT) {
            try {
                emit(Result.Loading)
                credentialManager.clearCredentialState(ClearCredentialStateRequest()) // Now it's accessible
                firebaseAuth.signOut()
                delay(200L)

                emit(Result.Success(Unit))
                Timber.e("SignOut Done id = %s", firebaseAuth.currentUser?.uid)
            } catch (e: Exception) {
                Timber.e("SignOut exception: $e")
                if (e is CancellationException) {
                    Timber.e("SignOut Cancellation: $e")
                    throw e
                    emit(Result.Error(e))
                } else {
                    emit(Result.Error(e))
                }
            }
        }
    }

    override suspend fun deleteUserFromAuth(activity: Activity): Flow<Result<Unit>> = flow {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            try {
                val googleIdToken = retrieveGoogleIdToken(activity)
                val credentials = buildGoogleAuthCredential(googleIdToken)

                // Re-authenticate
                currentUser.reauthenticate(credentials).await()

                // Delete the user after Re-authentication
                currentUser.delete().await()
                emit(Result.Success(Unit))
            } catch (e: Exception) {
                emit(Result.Error(Throwable("Error deleting user from Firebase Authentication: ${e.message}")))
            }
        } else {
            emit(Result.Error(Throwable("No user is signed in")))
        }
    }

    override suspend fun retrieveGoogleIdToken(context: Context): String {
        val credentialRequest =
            GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
        val credential =
            credentialManager.getCredential(request = credentialRequest, context = context)
        val googleIdTokenCredentialRequest =
            GoogleIdTokenCredential.createFrom(credential.credential.data)
        return googleIdTokenCredentialRequest.idToken
    }

    override fun buildGoogleAuthCredential(googleIdToken: String): AuthCredential {
        return GoogleAuthProvider.getCredential(googleIdToken, null)
    }

    companion object {
        private const val SIGN_IN = "SIGN_IN"
        private const val SIGN_OUT = "SIGN_OUT"
    }
}