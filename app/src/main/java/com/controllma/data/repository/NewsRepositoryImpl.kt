package com.controllma.data.repository

import com.controllma.data.network.response.NewResponse
import com.controllma.data.network.service.FirebaseDbService
import com.controllma.domain.interfaz.NewsRepository
import com.controllma.domain.model.NewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val firebaseDbService: FirebaseDbService
) : NewsRepository {

    override fun getAllNews(): Flow<List<NewResponse>> {
        return firebaseDbService.getAllNews()
    }

    override fun createNew(newResponse: NewModel): Boolean {
        return firebaseDbService.creteNew(newResponse)
    }
}