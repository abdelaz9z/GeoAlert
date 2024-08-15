package com.casecode.core.data.repository

import android.app.Activity
import android.content.Context
import com.casecode.core.common.result.Resource
import com.casecode.core.common.result.Result
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthService {
    val currentUserId: String
    val currentUser: Flow<FirebaseUser?>
    val hasUser: Boolean

    suspend fun signIn(activity: Activity): Resource<Int>
    suspend fun checkRegistration(email: String): Resource<Boolean>
    suspend fun isSignIn(): Resource<Boolean>
    suspend fun signOut(): Flow<Result<Unit>>
    suspend fun deleteUserFromAuth(activity: Activity): Flow<Result<Unit>>

    suspend fun retrieveGoogleIdToken(context: Context): String
    fun buildGoogleAuthCredential(googleIdToken: String): AuthCredential
}