package com.controllma.domain.usecases

import com.controllma.data.repository.NewsRepositoryImpl
import com.controllma.domain.model.NewModel
import javax.inject.Inject

class CreateNewUseCase @Inject constructor(
    private val newsRepositoryImpl: NewsRepositoryImpl
) {

    operator fun invoke(newResponse: NewModel): Boolean {
        return newsRepositoryImpl.createNew(newResponse)
    }
}