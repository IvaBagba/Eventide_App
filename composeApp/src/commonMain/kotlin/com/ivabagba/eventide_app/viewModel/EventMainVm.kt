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

    fun loadEvents(userID: Long) {

        //nos permite crear un subproceso que llamara a la api
        corrutine.launch {
            isLoading = true
            errorMessage = null

            try {
                events = eventApiService.getEventsByUser(userID)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Ha ocurrido un error al cargar eventos"
            } finally {
                isLoading = false
            }
        }

    }

    //Bloque eliminación de evento
    //Desactiva el boton borrar para evitar multiples llamadas a la api
    var isDeleting by mutableStateOf(false)
    private set
    //Muestra el mensaje de error al borrar un evento en caso de que falle
    var deleterError by mutableStateOf<String?>(null)
    private set

    fun deleteEvent(id: Long, onSuccess: () -> Unit,userID: Long) {
        corrutine.launch {
            try {
                isDeleting = true
                deleterError = null

                //Llamada a la api para borrar el evento
                eventApiService.deleteEvent(id)

                //Recargar lista post borrado
                loadEvents(userID)

                onSuccess()
            } catch (e: Exception) {
                deleterError = e.message ?: "Ha ocurrido un error al eliminar el evento"
            } finally {
                isDeleting = false
            }


        }
    }
}