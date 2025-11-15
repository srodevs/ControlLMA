package com.controllma.domain.interfaz

import com.controllma.data.network.response.RollCall
import kotlinx.coroutines.flow.Flow

interface RollCallRepository {

    suspend fun registerRollCall(rollCall: RollCall): Boolean
    suspend fun getAllRollCAll(uuid: String): Flow<List<RollCall>>

}