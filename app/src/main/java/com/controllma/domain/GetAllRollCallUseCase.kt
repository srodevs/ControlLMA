package com.controllma.domain

import com.controllma.data.model.RollCall
import com.controllma.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllRollCallUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(uuId: String): Flow<List<RollCall>> {
        return userRepository.getAllRollCAll(uuId)
    }

}