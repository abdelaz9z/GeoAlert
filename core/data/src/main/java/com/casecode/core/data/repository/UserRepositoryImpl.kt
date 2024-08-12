package com.casecode.core.data.repository

import com.casecode.core.common.result.Result
import com.casecode.core.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val usersRef: DatabaseReference,
    private val firebaseAuth: FirebaseAuth,
    private val googleAuthRepository: GoogleAuthRepository
) : UserRepository {

    override fun addUser(user: User): Flow<Result<Unit>> = flow {
        try {
            usersRef.child(user.id).setValue(user).await()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun deleteUserFromDatabase(): Flow<Result<Unit>> = flow {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            try {
                emit(Result.Loading)

                // Query to find the user with the given ID and remove it from the database
                val dataSnapshot = usersRef.orderByChild("id").equalTo(userId).get().await()
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val task = userSnapshot.ref.removeValue().await()
                        Timber.tag("test #1")
                            .d("User with ID $userId has been deleted successfully from database.")
                        emit(Result.Success(Unit))
                    }
                } else {
                    emit(Result.Error(Throwable("User with ID $userId does not exist in database.")))
                }
            } catch (e: Exception) {
                emit(Result.Error(Throwable("Error deleting user from database: ${e.message}")))
            }
        } else {
            emit(Result.Error(Throwable("No user is signed in")))
        }
    }

    override suspend fun deleteUserFromAuth(): Flow<Result<Unit>> = flow {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            try {
                val googleIdToken = googleAuthRepository.retrieveGoogleIdToken()
                val credentials = googleAuthRepository.buildGoogleAuthCredential(googleIdToken)

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

    override fun updateUser(user: User): Flow<Result<Unit>> = flow {
        try {
            usersRef.child(user.id).setValue(user).await()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun getUser(userId: String): Flow<Result<User>> = flow {
        try {
            val user = usersRef.child(userId).get().await().getValue(User::class.java)
            emit(Result.Success(user ?: throw Exception("User not found")))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun getUsers(): Flow<Result<List<User>>> = flow {
        try {
            val users =
                usersRef.get().await().children.mapNotNull { it.getValue(User::class.java) }
            emit(Result.Success(users))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
