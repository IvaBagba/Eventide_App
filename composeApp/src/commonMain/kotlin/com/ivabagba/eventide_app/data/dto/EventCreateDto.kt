package com.ivabagba.eventide_app.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventCreateDto (
    val eventName: String,
    val eventDesc: String,
    val eventDate: String,
    val eventTime: String,
    val eventLocation: String,
    val eventStatus: String,
    val cursosTags: List<String>
)

