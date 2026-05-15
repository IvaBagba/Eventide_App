package com.ivabagba.eventide_app.data.dto.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginReqDto(
    val dni: String,
    val userPass: String
)