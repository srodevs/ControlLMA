package com.controllma.domain

import com.controllma.data.model.RollCall
import com.controllma.data.repository.UserRepository
import javax.inject.Inject

class RegisterRollCallUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(rollCall: RollCall): Boolean {
        return userRepository.registerRollCall(rollCall)
    }
}