package com.casecode.core.domain

import android.app.Activity
import com.casecode.core.data.repository.AppLauncherRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val appLauncherRepository: AppLauncherRepository
) {
    suspend operator fun invoke(activity: Activity) = appLauncherRepository.signIn(activity)
}

class CheckRegistrationUseCase @Inject constructor(
    private val appLauncherRepository: AppLauncherRepository
) {
    suspend operator fun invoke(email: String) = appLauncherRepository.checkRegistration(email)
}

class IsSignInUseCase @Inject constructor(
    private val appLauncherRepository: AppLauncherRepository
) {
    suspend operator fun invoke() = appLauncherRepository.isSignIn()
}

class SignOutUseCase @Inject constructor(
    private val appLauncherRepository: AppLauncherRepository
) {
    suspend operator fun invoke() = appLauncherRepository.signOut()
}