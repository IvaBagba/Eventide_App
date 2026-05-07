package com.ivabagba.eventide_app.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventDataFieldsDto(
    val eventStatus: List<String>,
    val cursosTags: List<String>
) {

}