package com.casecode.feature.launcher.presentation.sign_in

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casecode.core.common.result.Resource
import com.casecode.core.domain.SignInUseCase
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

    fun signInWithGoogle(activity: Activity) {
        viewModelScope.launch {
            _signInResult.value = signInUseCase(activity)
        }
    }
}
