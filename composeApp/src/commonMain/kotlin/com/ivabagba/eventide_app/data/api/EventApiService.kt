package com.ivabagba.eventide_app.data.api

import com.ivabagba.eventide_app.data.dto.EventResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class EventApiService (
    private val client: HttpClient
) {
    //Suspend permite a la funcion estar pausada mientras se espera la respuesta del cliente HTTP
    suspend fun getEvents(): List<EventResponseDto>{
        return client.get("http://192.168.0.20:8081/eventide/events").body<List<EventResponseDto>>()
    }
}