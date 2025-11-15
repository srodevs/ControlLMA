package com.controllma.domain.usecases

import com.controllma.data.network.response.NewResponse
import com.controllma.data.repository.NewsRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNewsUseCase @Inject constructor(
    private val newsRepositoryImpl: NewsRepositoryImpl
) {

    operator fun invoke(): Flow<List<NewResponse>> {
        return newsRepositoryImpl.getAllNews()
    }

}