package com.ivabagba.eventide_app.data.api

import com.ivabagba.eventide_app.data.dto.EventCreateDto
import com.ivabagba.eventide_app.data.dto.EventDataFieldsDto
import com.ivabagba.eventide_app.data.dto.EventResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class EventApiService (
    private val client: HttpClient
) {
    //Suspend permite a la funcion estar pausada mientras se espera la respuesta del cliente HTTP
    suspend fun getEvents(): List<EventResponseDto>{
        return client.get("http://192.168.0.20:8081/eventide/events")
            .body<List<EventResponseDto>>()
    }

    suspend fun postEvent(event: EventCreateDto){
        client.post("http://192.168.0.20:8081/eventide/events") {
            contentType(ContentType.Application.Json)
            setBody(event)
        }
    }

    suspend fun getEventFieldData(): EventDataFieldsDto {
        return client.get("http://192.168.0.20:8081/eventide/data/eventFields")
        .body<EventDataFieldsDto>()
    }
}