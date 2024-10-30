package com.controllma.domain

import com.controllma.data.model.LoginResponse
import com.controllma.data.repository.UserRepository
import javax.inject.Inject

class DoLoginUserCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(email: String, pass: String): LoginResponse {
        return userRepository.login(email, pass)
    }
}