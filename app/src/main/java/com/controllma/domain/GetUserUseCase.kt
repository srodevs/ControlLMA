package com.controllma.domain

import com.controllma.data.model.UserResponse
import com.controllma.data.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): UserResponse? {
        return userRepository.getUserInf()
    }
}