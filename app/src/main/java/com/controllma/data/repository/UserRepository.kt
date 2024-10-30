package com.controllma.data.repository

import com.controllma.data.model.LoginResponse
import com.controllma.data.model.TypeLoginResponse
import com.controllma.data.service.FirebaseAuthService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuthService
) {

    suspend fun login(email: String, password: String): LoginResponse {
        val loginResponse = LoginResponse()
        try {
            val res: Boolean = firebaseAuth.loginUser(email, password)
            loginResponse.loginStatus =
                if (res) TypeLoginResponse.Success else TypeLoginResponse.Incorrect
            return loginResponse
        } catch (_: Exception) {
            return LoginResponse(loginStatus = TypeLoginResponse.Fail)
        }
    }
}