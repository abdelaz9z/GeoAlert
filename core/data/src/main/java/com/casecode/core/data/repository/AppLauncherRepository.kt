package com.casecode.core.data.repository

import android.app.Activity
import com.casecode.core.common.result.Resource

interface AppLauncherRepository {

    suspend fun signIn(activity: Activity): Resource<Int>

    suspend fun checkRegistration(email: String): Resource<Boolean>

    suspend fun isSignIn(): Resource<Boolean>

    suspend fun signOut()

}