package com.controllma.domain.usecases

import com.controllma.data.network.response.RollCall
import com.controllma.data.repository.RollCallImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllRollCallUseCase @Inject constructor(
    private val rollCallImpl: RollCallImpl
) {

    suspend operator fun invoke(uuId: String): Flow<List<RollCall>> {
        return rollCallImpl.getAllRollCAll(uuId)
    }

}