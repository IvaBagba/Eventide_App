package com.ivabagba.eventide_app.data.dto.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginResDto (
    val id: Long,
    val dni: String,
    val userName: String,
    val userSurname: String? = null,
    val userRole : String,
    val cursosTags: List<String>
)