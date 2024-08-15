package com.casecode.core.domain

import android.app.Activity
import com.casecode.core.common.result.Result
import com.casecode.core.data.repository.AuthService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authService: AuthService
) {
    suspend operator fun invoke(activity: Activity) = authService.signIn(activity)
}

class SignOutUseCase @Inject constructor(
    private val authService: AuthService
) {
    suspend operator fun invoke(): Flow<Result<Unit>> = authService.signOut()
}

class IsSignInUseCase @Inject constructor(
    private val authService: AuthService
) {
    suspend operator fun invoke() = authService.isSignIn()
}

class CheckRegistrationUseCase @Inject constructor(
    private val authService: AuthService
) {
    suspend operator fun invoke(email: String) = authService.checkRegistration(email)
}

class DeleteUserFromAuthUseCase @Inject constructor(
    private val authService: AuthService
) {
    suspend operator fun invoke(activity: Activity) =
        authService.deleteUserFromAuth(activity)
}

class GetCurrentUserUseCase @Inject constructor(
    private val authService: AuthService
) {
    operator fun invoke() = authService.currentUser
}