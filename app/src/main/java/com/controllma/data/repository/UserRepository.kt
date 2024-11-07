package com.controllma.data.repository

import com.controllma.data.model.LoginResponse
import com.controllma.data.model.NewResponse
import com.controllma.data.model.TypeLoginResponse
import com.controllma.data.model.UserResponse
import com.controllma.data.service.FirebaseAuthService
import com.controllma.data.service.FirebaseDbService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuthService,
    private val firebaseDbService: FirebaseDbService
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

    fun logOut() {
        firebaseAuth.logOut()
    }

    suspend fun getUserInf(): UserResponse? {
        return firebaseDbService.getUserInf(firebaseAuth.getCurrentUser().toString())
    }

    fun getAllNews(): Flow<List<NewResponse>> {
        return firebaseDbService.getAllNews()
    }

}