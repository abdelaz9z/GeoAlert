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
import com.google.android.gms.common.api.UnsupportedApiCallException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class AppLauncherRepositoryImp @Inject constructor(
    @ApplicationContext private val context: Context,
    private val googleIdOption: GetGoogleIdOption,
    private val firebaseAuth: FirebaseAuth,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : AppLauncherRepository {
    private val TAG = "AccountServiceImpl"
    private val credentialManager: CredentialManager = CredentialManager.create(context)
    override suspend fun signIn(activity: Activity): Resource<Int> {
        trace(SIGN_IN) {
            return withContext(ioDispatcher) {
                try {
                    val googleIdToken = retrieveGoogleIdToken(activity)
                    val googleCredentials = buildGoogleAuthCredential(googleIdToken)
                    val authResult = signInWithGoogleCredentials(googleCredentials)

                    if (authResult.user != null) {
                        Resource.success(R.string.sign_in_success)
                    } else {
                        Resource.empty(null, R.string.sign_in_failure)
                    }
                } catch (e: GetCredentialException) {
                    Timber.e("Sign-in failed with GetCredentialException: ${e.message}")
                    Resource.error(R.string.sign_in_exception)
                } catch (e: UnsupportedApiCallException) {
                    Timber.e("Sign-in failed with UnsupportedApiCallException: ${e.message}")
                    Resource.error(R.string.unsupported_api_call)
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    Resource.error(e.message)
                } catch (e: FirebaseAuthException) {
                    Timber.e("Sign-in failed with FirebaseAuthException: ${e.message}")
                    Resource.error(R.string.sign_in_api_exception)
                } catch (e: Exception) {
                    Timber.e("Sign-in failed with exception: ${e.message}")
                    Resource.error(R.string.sign_in_failure)
                }
            }
        }
    }

    private suspend fun retrieveGoogleIdToken(activity: Activity): String {
        val credentialRequest =
            GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
        val credential =
            credentialManager.getCredential(request = credentialRequest, context = activity)
        val googleIdTokenCredentialRequest =
            GoogleIdTokenCredential.createFrom(credential.credential.data)
        return googleIdTokenCredentialRequest.idToken
    }

    private fun buildGoogleAuthCredential(googleIdToken: String): AuthCredential {
        return GoogleAuthProvider.getCredential(googleIdToken, null)
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
                Timber.tag(TAG).e("checkRegistration: email is created before :true")
                Resource.Success(true)
            } catch (e: FirebaseAuthUserCollisionException) {
                Timber.tag(TAG).e("checkRegistration: email is created before :false")
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
                    Timber.tag("test #1").e("currentUser: true")
                    Resource.success(true)
                } else {
                    Timber.tag("test #1").e("currentUser: null")
                    Resource.empty(null, false)
                }
            } catch (e: Exception) {
                Timber.tag("test #1").e("isSignIn: ${e.message}")
                Resource.error(e.message)
            }
        }
    }

    override suspend fun signOut(): Flow<Result<Unit>> = flow {
        trace(SIGN_OUT) {
            try {
                emit(Result.Loading)
                credentialManager.clearCredentialState(ClearCredentialStateRequest())
                firebaseAuth.signOut()
                delay(200L)

                emit(Result.Success(Unit))
                Timber.tag(TAG).e("SignOut Done id = %s", firebaseAuth.currentUser?.uid)
            } catch (e: Exception) {
                Timber.tag(TAG).e("SignOut exception: $e")
                if (e is CancellationException) {
                    Timber.tag(TAG).e("SignOut Cancellation: $e")
                    throw e
                    emit(Result.Error(e))
                } else {
                    Timber.tag(TAG).e("SignOut exception: $e")
                    emit(Result.Error(e))
                }
            }
        }
    }

    companion object {
        private const val SIGN_IN = "SIGN_IN"
        private const val SIGN_OUT = "SIGN_OUT"
    }

}