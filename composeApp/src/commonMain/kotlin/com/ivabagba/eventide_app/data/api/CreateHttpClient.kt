package com.ivabagba.eventide_app.data.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

//creamos y configuramos el cliente HTTP que va a hacer las peticiones
fun CreateHttpClient(): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(
                Json { ignoreUnknownKeys = true }
            )
        }
    }
}