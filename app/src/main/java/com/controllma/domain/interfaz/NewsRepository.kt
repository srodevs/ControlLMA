package com.controllma.domain.interfaz

import com.controllma.data.network.response.NewResponse
import com.controllma.domain.model.NewModel
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getAllNews(): Flow<List<NewResponse>>
    fun createNew(newResponse: NewModel): Boolean

}