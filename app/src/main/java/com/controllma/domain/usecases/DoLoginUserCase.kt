package com.controllma.domain.usecases

import com.controllma.domain.model.LoginResultModel
import com.controllma.data.repository.UserRepositoryImpl
import javax.inject.Inject

class DoLoginUserCase @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl
) {

    suspend operator fun invoke(email: String, pass: String): LoginResultModel {
        return userRepositoryImpl.login(email, pass)
    }
}