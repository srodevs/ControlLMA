package com.controllma.domain.usecases

import com.controllma.data.network.response.RollCall
import com.controllma.data.repository.RollCallImpl
import javax.inject.Inject

class RegisterRollCallUseCase @Inject constructor(
    private val rollCallImpl: RollCallImpl
) {

    suspend operator fun invoke(rollCall: RollCall): Boolean {
        return rollCallImpl.registerRollCall(rollCall)
    }
}