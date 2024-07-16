package com.casecode.feature.launcher.presentation.sign_in.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casecode.feature.launcher.domain.usecase.SignInUseCase
import com.casecode.feature.launcher.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _signInResult = MutableStateFlow<Resource<Int>>(Resource.Loading)
    val signInResult: StateFlow<Resource<Int>> = _signInResult

    fun signIn(activity: Activity) {
        viewModelScope.launch {
            _signInResult.value = signInUseCase(activity)
        }
    }
}
