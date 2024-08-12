package com.casecode.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casecode.core.common.result.Result
import com.casecode.core.domain.DeleteUserUseCase
import com.casecode.core.domain.GetCurrentUserUseCase
import com.casecode.core.domain.SignOutUseCase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Account screen.
 */
@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> get() = _currentUser

    private val _deleteUserResult = MutableStateFlow<Result<Unit>?>(null)
    val deleteUserResult: StateFlow<Result<Unit>?> = _deleteUserResult

    private val _signOutResult = MutableStateFlow<Result<Unit>?>(null)
    val signOutResult: StateFlow<Result<Unit>?> = _signOutResult

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                _currentUser.value = user
            }
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            deleteUserUseCase.deleteUserFromDatabase().collect { databaseResult ->
                handleDeleteResult(databaseResult)
            }
        }
    }

    private suspend fun handleDeleteResult(databaseResult: Result<Unit>) {
        if (databaseResult is Result.Success) {
            deleteUserUseCase.deleteUserFromAuth().collect { authResult ->
                _deleteUserResult.value = authResult
            }
        } else {
            _deleteUserResult.value = databaseResult
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase().collect { result ->
                _signOutResult.value = result
            }
        }
    }

    // Add methods to reset the results
    fun resetSignOutResult() {
        _signOutResult.value = null
    }

    fun resetDeleteUserResult() {
        _deleteUserResult.value = null
    }
}
