package com.controllma.data.repository

import com.controllma.data.network.response.RollCall
import com.controllma.data.network.service.FirebaseDbService
import com.controllma.domain.interfaz.RollCallRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RollCallImpl @Inject constructor(
    private val firebaseDbService: FirebaseDbService
) : RollCallRepository {

    override suspend fun registerRollCall(rollCall: RollCall): Boolean {
        return try {
            firebaseDbService.createRollCall(rollCall)
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getAllRollCAll(uuid: String): Flow<List<RollCall>> {
        return firebaseDbService.getMyRollCall(uuid)
    }
}