package com.ivabagba.eventide_app.data.api

import com.ivabagba.eventide_app.data.dto.EventCreateDto
import com.ivabagba.eventide_app.data.dto.EventDataFieldsDto
import com.ivabagba.eventide_app.data.dto.EventResponseDto
import com.ivabagba.eventide_app.data.dto.login.LoginReqDto
import com.ivabagba.eventide_app.data.dto.login.LoginResDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class EventApiService (
    private val client: HttpClient
) {

    var serverUrl: String = "https://eventideapi-production-162a.up.railway.app"
    //Suspend permite a la funcion estar pausada mientras se espera la respuesta del cliente HTTP
    suspend fun getEvents(): List<EventResponseDto>{
        return client.get("$serverUrl/eventide/events")
            .body<List<EventResponseDto>>()
    }

    suspend fun postEvent(event: EventCreateDto){
        client.post("$serverUrl/eventide/events") {
            contentType(ContentType.Application.Json)
            setBody(event)
        }
    }

    suspend fun getEventFieldData(): EventDataFieldsDto {
        return client.get("$serverUrl/eventide/data/eventFields")
        .body<EventDataFieldsDto>()
    }

    suspend fun deleteEvent(id: Long){
        client.delete("$serverUrl/eventide/events/$id") {}
    }

    suspend fun updateEvent(
        id: Long,
        event: EventCreateDto
    ) : EventResponseDto {
        return client.put("$serverUrl/eventide/events/$id") {
            contentType(ContentType.Application.Json)
            setBody(event)
        }.body()
    }

    suspend fun login(request: LoginReqDto): LoginResDto{

        val response = client.post("$serverUrl/eventide/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if(response.status != HttpStatusCode.OK){
            throw Exception("DNI o Contraseña incorretos")
        }

        return response.body()
    }
}