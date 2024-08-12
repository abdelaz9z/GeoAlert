package com.casecode.core.domain

import com.casecode.core.data.repository.GoogleAuthRepository
import javax.inject.Inject

class GoogleAuthUseCase @Inject constructor(private val googleAuthRepository: GoogleAuthRepository) {
    suspend fun retrieveGoogleIdToken() = googleAuthRepository.retrieveGoogleIdToken()
    fun buildGoogleAuthCredential(googleIdToken: String) =
        googleAuthRepository.buildGoogleAuthCredential(googleIdToken)

}