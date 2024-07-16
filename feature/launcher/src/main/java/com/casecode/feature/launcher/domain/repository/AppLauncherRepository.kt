package com.casecode.feature.launcher.domain.repository

import android.app.Activity
import com.casecode.feature.launcher.domain.utils.Resource


interface AppLauncherRepository {

    suspend fun signIn(activity: Activity): Resource<Int>

    suspend fun checkRegistration(email: String): Resource<Boolean>

    suspend fun isSignIn(): Resource<Boolean>

    suspend fun signOut()

}