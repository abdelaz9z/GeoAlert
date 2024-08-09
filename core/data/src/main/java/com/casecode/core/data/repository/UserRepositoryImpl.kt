package com.casecode.core.data.repository

import com.casecode.core.common.result.Result
import com.casecode.core.data.model.User
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val usersRef: DatabaseReference
) : UserRepository {

    override fun addUser(user: User): Flow<Result<Unit>> = flow {
        try {
            usersRef.child(user.id).setValue(user).await()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun deleteUser(userId: String): Flow<Result<Unit>> = flow {
        try {
            usersRef.child(userId).removeValue().await()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
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
            val users = usersRef.get().await().children.mapNotNull { it.getValue(User::class.java) }
            emit(Result.Success(users))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
