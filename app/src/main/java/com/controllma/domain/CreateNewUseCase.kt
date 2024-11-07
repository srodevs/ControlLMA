package com.controllma.domain

import com.controllma.data.model.NewModel
import com.controllma.data.repository.UserRepository
import javax.inject.Inject

class CreateNewUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(newResponse: NewModel): Boolean {
        return userRepository.createNew(newResponse)
    }
}