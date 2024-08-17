package com.casecode.core.data.repository

import com.casecode.core.common.network.Dispatcher
import com.casecode.core.common.network.GeoAlertDispatchersDispatchers.IO
import com.casecode.core.common.result.Result
import com.casecode.core.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val usersRef: DatabaseReference,
    private val firebaseAuth: FirebaseAuth,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : UserRepository {

    override suspend fun addUser(user: User): Result<Unit> {
        return withContext(ioDispatcher) {
            try {
                usersRef.child(user.id).setValue(user).await()
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun deleteUser(): Flow<Result<Unit>> = flow {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            try {
                emit(Result.Loading)

                // Check if the user exists in the database
                val userSnapshot = usersRef.child(userId).get().await()
                if (userSnapshot.exists()) {
                    // Attempt to remove the user if they exist
                    usersRef.child(userId).removeValue().await()
                    emit(Result.Success(Unit))
                } else {
                    // Emit error if the user does not exist in the database
                    emit(Result.Error(Throwable("User does not exist in the database")))
                }
            } catch (e: Exception) {
                // Emit error if the deletion process fails
                emit(Result.Error(Throwable("Error deleting user from database: ${e.message}")))
            }
        } else {
            // Emit error if there is no signed-in user
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
