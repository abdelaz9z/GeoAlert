package com.casecode.core.data.repository

import com.google.firebase.auth.AuthCredential

interface GoogleAuthRepository {
    suspend fun retrieveGoogleIdToken(): String
    fun buildGoogleAuthCredential(googleIdToken: String): AuthCredential
}
