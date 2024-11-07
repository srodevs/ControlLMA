package com.controllma.domain

import com.controllma.data.model.NewResponse
import com.controllma.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNewsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(): Flow<List<NewResponse>> {
        return userRepository.getAllNews()
    }

}