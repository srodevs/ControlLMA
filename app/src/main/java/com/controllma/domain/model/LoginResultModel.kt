package com.controllma.domain.model

import com.controllma.core.TypeLoginResponse

data class LoginResultModel(
    var loginStatus: TypeLoginResponse = TypeLoginResponse.Incorrect,
    var msg: String = ""
)