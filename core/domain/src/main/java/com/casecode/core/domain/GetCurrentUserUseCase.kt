package com.casecode.core.domain

import com.casecode.core.data.repository.AuthService
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(private val authService: AuthService) {
    operator fun invoke () = authService.currentUser
}