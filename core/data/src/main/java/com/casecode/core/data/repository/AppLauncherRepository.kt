package com.casecode.core.data.repository

import android.app.Activity
import com.casecode.core.common.result.Resource
import kotlinx.coroutines.flow.Flow
import com.casecode.core.common.result.Result

interface AppLauncherRepository {

    suspend fun signIn(): Resource<Int>

    suspend fun checkRegistration(email: String): Resource<Boolean>

    suspend fun isSignIn(): Resource<Boolean>

    suspend fun signOut(): Flow<Result<Unit>>

}