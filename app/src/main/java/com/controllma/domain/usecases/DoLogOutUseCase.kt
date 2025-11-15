package com.controllma.domain.usecases

import com.controllma.data.repository.UserRepositoryImpl
import javax.inject.Inject

class DoLogOutUseCase @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl
) {

    operator fun invoke() {
        return userRepositoryImpl.logOut()
    }
}