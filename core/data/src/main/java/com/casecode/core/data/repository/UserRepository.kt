package com.casecode.core.data.repository

import android.app.Activity
import com.casecode.core.common.result.Result
import com.casecode.core.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun addUser(user: User): Result<Unit>
    suspend fun deleteUser(): Flow<Result<Unit>>
    fun updateUser(user: User): Flow<Result<Unit>>
    fun getUser(userId: String): Flow<Result<User>>
    fun getUsers(): Flow<Result<List<User>>>
}