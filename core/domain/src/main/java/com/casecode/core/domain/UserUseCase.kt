package com.casecode.core.domain

import com.casecode.core.common.result.Result
import com.casecode.core.data.model.User
import com.casecode.core.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(user: User): Flow<Result<Unit>> = userRepository.addUser(user)
}

class DeleteUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(userId: String): Flow<Result<Unit>> = userRepository.deleteUser(userId)
}

class UpdateUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(user: User): Flow<Result<Unit>> = userRepository.updateUser(user)
}

class GetUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(userId: String): Flow<Result<User>> = userRepository.getUser(userId)
}

class GetUsersUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(): Flow<Result<List<User>>> = userRepository.getUsers()
}

