package com.casecode.feature.launcher.presentation.splash.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casecode.feature.launcher.domain.usecase.IsSignInUseCase
import com.casecode.feature.launcher.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isSignInUseCase: IsSignInUseCase,
) : ViewModel() {

    private val _signInStatus = MutableStateFlow<Resource<Boolean>>(Resource.Loading)
    val signInStatus: StateFlow<Resource<Boolean>> = _signInStatus

    init {
        checkSignInStatus()
    }

    private fun checkSignInStatus() {
        viewModelScope.launch {
            _signInStatus.value = isSignInUseCase()
        }
    }
}