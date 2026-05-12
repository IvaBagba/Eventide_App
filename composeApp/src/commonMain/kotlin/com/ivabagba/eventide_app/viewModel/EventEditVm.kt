package com.ivabagba.eventide_app.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ivabagba.eventide_app.data.api.EventApiService
import com.ivabagba.eventide_app.data.dto.EventCreateDto
import com.ivabagba.eventide_app.data.dto.EventResponseDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class EventEditVm(
    private val eventApiService: EventApiService,
    event: EventResponseDto
) {

    var eventName by mutableStateOf(event.eventName)
    var eventDesc by mutableStateOf(event.eventDesc ?: "")
    var eventDate by mutableStateOf(event.eventDate)
    var eventTime by mutableStateOf(event.eventTime)
    var eventLocation by mutableStateOf(event.eventLocation)
    var eventStatus by mutableStateOf(event.eventStatus)
    var cursosTags by mutableStateOf<List<String>>(event.cursosTags)

    var statusData by mutableStateOf<List<String>>(emptyList())
    private set

    var cursosTagData by mutableStateOf<List<String>>(emptyList())
    private set

    var isSaving by mutableStateOf(false)
    private set

    var errorMessage by mutableStateOf<String?>(null)
    private set

    var isSuccess by mutableStateOf(false)
    private set

    var isLoading by mutableStateOf(false)
    private set

    var dataError by mutableStateOf<String?>(null)
    private set

    var formError by mutableStateOf<String?>(null)
    private set

    val corrutine = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    suspend fun loadDataFields() {
        corrutine.launch {
            isLoading = true
            dataError = ""
        }

        try {
            val data = eventApiService.getEventFieldData()

            statusData = data.eventStatus
            cursosTagData = data.cursosTags

            if (eventStatus.isBlank() && data.eventStatus.isNotEmpty()) {
                eventStatus = data.eventStatus.first()
            }

        } catch (e: Exception) {
            dataError = e.message ?: "Error al cargar los datos"
        } finally {
            isLoading = false
        }
    }

    fun updateEvent(id:Long){
        //Llamada a metodo de validacion de campos
        if (!validateFields()) return

        corrutine.launch {
            isSaving = true
            errorMessage = null
            isSuccess = false

            try {
                val eventUpdate = EventCreateDto(
                    eventName = eventName.trim(),
                    eventDesc = eventDesc.trim(),
                    eventDate = eventDate.trim(),
                    eventTime = eventTime.trim(),
                    eventLocation = eventLocation.trim(),
                    cursosTags = cursosTags,
                    eventStatus = eventStatus,
                )

                eventApiService.updateEvent(
                    id = id,
                    event = eventUpdate
                )

                isSuccess = true

            } catch (e: Exception) {
                errorMessage = e.message ?: "Error al editar el evento"
            } finally {
                isSaving = false
            }
        }
    }

    private fun validateFields(): Boolean {
        formError = null

        if (eventName.isBlank()){
            formError = "El nombre del evento no puede estar vacio"
            return false
        }

        if (eventDate.isBlank()){
            formError = "La fecha no puede estar vacia"
            return false
        }

        try {
            LocalDate.parse(eventDate)
        } catch (e:Exception) {
            formError = "El formato es invalido, usa (YYYY-MM-DD)"
            return false
        }

        if (eventTime.isBlank()){
            formError = "La hora no puede estar vacia"
            return false
        }

        try {
            LocalTime.parse(eventTime)
        } catch (e:Exception) {
            formError = "El formato es invalido, usa (HH:mm:ss)"
            return false
        }

        if (eventLocation.isBlank()){
            formError = "La Ubicación no puede estar vacia"
            return false
        }

        if (eventStatus.isBlank()){
            formError = "No hay estado Seleccionado"
            return false
        }

        if (cursosTags.isNullOrEmpty()){
            formError = "No hay Tags Establecidas"
            return false
        }

        return true
    }


}