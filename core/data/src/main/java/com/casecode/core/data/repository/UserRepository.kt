package com.casecode.core.data.repository

import android.app.Activity
import com.casecode.core.common.result.Result
import com.casecode.core.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun addUser(user: User): Flow<Result<Unit>>
    suspend fun deleteUserFromDatabase(): Flow<Result<Unit>>
    fun updateUser(user: User): Flow<Result<Unit>>
    fun getUser(userId: String): Flow<Result<User>>
    fun getUsers(): Flow<Result<List<User>>>
}