package com.ivabagba.eventide_app.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ivabagba.eventide_app.data.api.EventApiService
import com.ivabagba.eventide_app.data.dto.EventResponseDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class EventMainVm (
    private val eventApiService: EventApiService
){
    //Al principio es una lista vacia y luego cuando tenemos los evenos de la api se añaden a la lista
    var events by mutableStateOf<List<EventResponseDto>>(emptyList())
    private set

    //Nos dice si la pantalla esta cargando (con esto mostramos la animacion de carga en caso de que sea necesaria)
    var isLoading by mutableStateOf(false)
    private set

    //si tenemos algun error como una perdida de conexion al servidor ahi mostramos el mensaje de error
    var errorMessage by mutableStateOf<String?>(null)
    private set

    private val corrutine = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun loadEvents() {

        //nos permite crear un subproceso que llamara a la api
        corrutine.launch {
            isLoading = true
            errorMessage = null

            try {
                events = eventApiService.getEvents()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Ha ocurrido un error"
            } finally {
                isLoading = false
            }
        }

    }
}