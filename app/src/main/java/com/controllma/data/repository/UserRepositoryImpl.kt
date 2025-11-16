package com.controllma.data.repository

import com.controllma.core.TypeLoginResponse
import com.controllma.data.network.response.UserResponse
import com.controllma.data.network.service.FirebaseAuthService
import com.controllma.data.network.service.FirebaseDbService
import com.controllma.domain.interfaz.UserRepository
import com.controllma.domain.model.LoginResultModel
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuthService,
    private val firebaseDbService: FirebaseDbService
) : UserRepository {

    override suspend fun login(email: String, password: String): LoginResultModel {
        val loginResultModel = LoginResultModel()
        try {
            val res: Boolean = firebaseAuth.loginUser(email, password)
            loginResultModel.loginStatus =
                if (res) TypeLoginResponse.Success else TypeLoginResponse.Incorrect
            return loginResultModel
        } catch (_: Exception) {
            return LoginResultModel(loginStatus = TypeLoginResponse.Fail)
        }
    }

    override fun logOut() {
        firebaseAuth.logOut()
    }

    override suspend fun getUserInf(): UserResponse? {
        return firebaseDbService.getUserInf(firebaseAuth.getCurrentUser().toString())
    }

}