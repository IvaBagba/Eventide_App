package com.ivabagba.eventide_app.data

import com.ivabagba.eventide_app.data.dto.EventResponseDto
import com.ivabagba.eventide_app.viewModel.EventMainVm

class EventApiService (
    private val client: HttpClient
) {
    //Suspend permite a la funcion estar pausada mientras se espera la respuesta del cliente HTTP
    suspend fun getEvents(): List<EventResponseDto>{
        return client.get("localhost/eventide/events").body()
    }
}