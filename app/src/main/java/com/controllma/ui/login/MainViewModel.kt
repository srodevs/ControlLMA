package com.controllma.ui.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.controllma.data.model.LoginResponse
import com.controllma.data.model.UserResponse
import com.controllma.domain.DoLogOutUseCase
import com.controllma.domain.DoLoginUserCase
import com.controllma.domain.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginUseCase: DoLoginUserCase,
    private val getUserUseCase: GetUserUseCase,
    private val logoutUseCase: DoLogOutUseCase
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    private val _pass = MutableStateFlow("")
    val pass = _pass.asStateFlow()
    private val _btnEnable = MutableStateFlow(false)
    val btnEnable = _btnEnable.asStateFlow()
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun onLoginChange(email: String, pass: String) {
        _email.value = email
        _pass.value = pass
        _btnEnable.value = enableLogin(email, pass)
    }

    fun onLoginSelected(responseLogin: (LoginResponse) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            val res = loginUseCase(email.value, pass.value)
            responseLogin(res)
            Log.e(
                "viewModel", """
                -> ${email.value} 
                -> ${pass.value}
                -> $res
                """.trimIndent()
            )
            _loading.value = false
        }
    }

    private fun enableLogin(email: String, pass: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() && pass.length > 8

    fun getUserInf(userModel: (UserResponse?) -> Unit) {
        viewModelScope.launch {
            val res = getUserUseCase.invoke()
            userModel(res)
        }
    }

    fun logOut(response: (Boolean) -> Unit) {
        viewModelScope.launch {
            logoutUseCase.invoke()
            response(true)
        }
    }
}