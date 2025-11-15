package com.controllma.ui.views

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.controllma.core.StorageUser
import com.controllma.data.network.response.NewResponse
import com.controllma.data.network.response.RollCall
import com.controllma.data.network.response.UserResponse
import com.controllma.domain.model.LoginResultModel
import com.controllma.domain.model.NewModel
import com.controllma.domain.usecases.CreateNewUseCase
import com.controllma.domain.usecases.DoLogOutUseCase
import com.controllma.domain.usecases.DoLoginUserCase
import com.controllma.domain.usecases.GetAllNewsUseCase
import com.controllma.domain.usecases.GetAllRollCallUseCase
import com.controllma.domain.usecases.GetUserUseCase
import com.controllma.domain.usecases.RegisterRollCallUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val storageUser: StorageUser,
    private val loginUseCase: DoLoginUserCase,
    private val getUserUseCase: GetUserUseCase,
    private val logoutUseCase: DoLogOutUseCase,
    private val getAllNewsUseCase: GetAllNewsUseCase,
    private val createNewUseCase: CreateNewUseCase,
    private val registerRollCallUseCase: RegisterRollCallUseCase,
    private val getAllRollCallUseCase: GetAllRollCallUseCase
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    private val _pass = MutableStateFlow("")
    val pass = _pass.asStateFlow()
    private val _btnEnable = MutableStateFlow(false)
    val btnEnable = _btnEnable.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _newTitle = MutableStateFlow("")
    val newTitle = _newTitle.asStateFlow()
    private val _newDescr = MutableStateFlow("")
    val newDescr = _newDescr.asStateFlow()

    private val _profilePassword = MutableStateFlow("")
    val profilePassword = _profilePassword.asStateFlow()

    private var _newsList = MutableStateFlow<List<NewResponse>>(emptyList())
    val newsList: StateFlow<List<NewResponse>> = _newsList

    fun onLoginChange(email: String, pass: String) {
        _email.value = email
        _pass.value = pass
        _btnEnable.value = enableLogin(email, pass)
    }

    fun onLoginSelected(responseLogin: (LoginResultModel) -> Unit) {
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
            _loading.value = true
            val res = getUserUseCase.invoke()
            userModel(res)
            _loading.value = false
        }
    }

    fun logOut(response: (Boolean) -> Unit) {
        viewModelScope.launch {
            logoutUseCase.invoke()
            response(true)
        }
    }

    fun getAllNews() {
        viewModelScope.launch {
            _loading.value = true
            getAllNewsUseCase().collect {
                _newsList.value = it
                _loading.value = false
            }
        }
    }

    fun onNewsChange(title: String, description: String) {
        _newTitle.value = title
        _newDescr.value = description
    }

    fun onPublish(uuId: String, res: (Boolean) -> Unit) {
        viewModelScope.launch {
            val new = NewModel(
                title = newTitle.value,
                description = newDescr.value,
                timestamp = System.currentTimeMillis(),
                newId = "",
                urlImage = null,
                publisher = uuId
            )
            val response = createNewUseCase.invoke(new)
            res(response)
        }
    }

    fun onProfileChangePass(pass: String) {
        _profilePassword.value = pass
    }

    fun loginFromProfile(email: String, responseLogin: (LoginResultModel) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            val res: LoginResultModel = loginUseCase(email, profilePassword.value)
            responseLogin(res)
            _loading.value = false
        }
    }

    fun createRollCall(myUuid: String, response: (Boolean) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            val rollCall = RollCall(
                uuId = myUuid,
                timestamp = System.currentTimeMillis()
            )
            val res: Boolean = registerRollCallUseCase.invoke(rollCall)
            response(res)
            _loading.value = false
        }
    }

    fun getAllRollCall(uuId: String) {
        viewModelScope.launch {
            getAllRollCallUseCase.invoke(uuId).collect {
                Log.e("main", it.toString())
            }
        }
    }

    fun storageIsLoggedIn() = storageUser.getLogin()
    fun saveLoginBool(bool: Boolean) {
        viewModelScope.launch {
            storageUser.saveLoginBool(bool)
        }
    }

    fun saveUserInfo(
        uuid: String,
        email: String,
        username: String,
        userType: String,
        userImage: String,
        tokenFcm: String
    ) {
        viewModelScope.launch {
            storageUser.saveUserInfo(
                uuid,
                email,
                username,
                userType,
                userImage,
                tokenFcm
            )
        }
    }

    fun getUserType(): Flow<String> = storageUser.getUserType()
    fun getUserUuid() = storageUser.getUserUuid()
    fun getUserImage() = storageUser.getUserImage()
    fun getUsername() = storageUser.getUsername()
    fun getUserEmail() = storageUser.getUserEmail()



}