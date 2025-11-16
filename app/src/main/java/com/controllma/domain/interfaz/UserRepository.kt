package com.controllma.domain.interfaz

import com.controllma.data.network.response.UserResponse
import com.controllma.domain.model.LoginResultModel

interface UserRepository {
    suspend fun login(email: String, password: String): LoginResultModel
    fun logOut()
    suspend fun getUserInf(): UserResponse?

}