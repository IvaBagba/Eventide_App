package com.ivabagba.eventide_app.data.dto

import kotlinx.serialization.Serializable

//Permite convertir los datos de JSON a una clase Kotlin
@Serializable
data class EventResponseDto(
    val id: Long,
    val eventName: String,
    val eventDesc: String? = null,
    val eventDate: String,
    val eventTime: String,
    val eventLocation: String,
    val eventStatus: String,
    val cursosTags: List<String> = emptyList(),
    val regUsersID: List<Long> = emptyList(),
)