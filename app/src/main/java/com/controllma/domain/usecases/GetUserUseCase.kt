package com.controllma.domain.usecases

import com.controllma.data.network.response.UserResponse
import com.controllma.data.repository.UserRepositoryImpl
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl
) {

    suspend operator fun invoke(): UserResponse? {
        return userRepositoryImpl.getUserInf()
    }
}