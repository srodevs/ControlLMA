package com.controllma.data.model

data class LoginResponse(
    var loginStatus: TypeLoginResponse = TypeLoginResponse.Incorrect,
    var msg: String = ""
)

enum class TypeLoginResponse {
    Success,
    Incorrect,
    Fail
}