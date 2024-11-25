package com.controllma.domain

import com.controllma.data.repository.UserRepository
import javax.inject.Inject

class DoLogOutUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke() {
        return userRepository.logOut()
    }
}