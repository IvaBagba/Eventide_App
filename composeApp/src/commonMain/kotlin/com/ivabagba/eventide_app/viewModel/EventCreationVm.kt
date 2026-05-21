package com.ivabagba.eventide_app.viewModel

import androidx.compose.material3.LocalShortNavigationBarOverride
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ivabagba.eventide_app.data.api.EventApiService
import com.ivabagba.eventide_app.data.dto.EventCreateDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class EventCreationVm(
    private val eventApiService: EventApiService
) {

    //Valores de los campos a rellenar
    var eventName by mutableStateOf("")
    var eventDesc by mutableStateOf("")
    var eventDate by mutableStateOf("")
    var eventTime by mutableStateOf("")
    var eventLocation by mutableStateOf("")
    var eventStatus by mutableStateOf("")
    var cursosTags by mutableStateOf<List<String>>(emptyList())

    //Valores de los campos recibidos por la api
    var statusData by mutableStateOf<List<String>>(emptyList())
        private set
    var cursosTagData by mutableStateOf<List<String>>(emptyList())
        private set

    //Nos dice si la pantalla está cargando (con esto mostramos la animacion de carga en caso de que sea necesaria)
    var isSaving by mutableStateOf(false)
        private set

    //si tenemos algún error como una perdida de conexion al servidor ahi mostramos el mensaje de error
    var errorMessage by mutableStateOf<String?>(null)
        private set

    //Una vez enviada la petición nos dice si el evento ha sido creado correctamente
    var isSuccess by mutableStateOf(false)

    private val corrutine = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun createEvent(userID: Long) {
        if (!validateFields()) return

        corrutine.launch {
            isSaving = true
            errorMessage = null
            isSuccess = false

            try {
                val eventCreate = EventCreateDto(
                    eventName = eventName.trim(),
                    eventDesc = eventDesc.trim(),
                    eventDate = eventDate.trim(),
                    eventTime = eventTime.trim(),
                    eventLocation = eventLocation.trim(),
                    eventStatus = eventStatus,
                    cursosTags = cursosTags
                )

                eventApiService.postEvent(eventCreate, userID)
                isSuccess = true

            } catch (e:Exception) {
                errorMessage = e.message ?: "Error al crear el evento"
            } finally {
                isSaving = false
            }
        }
    }

    var isLoadingData by mutableStateOf(false)
    private set
    var dataError by mutableStateOf<String?>(null)

    suspend fun loadEventDataFields() {
        corrutine.launch {
            isLoadingData = true
            dataError = null
        }

        try {
            val data = eventApiService.getEventFieldData()

            statusData = data.eventStatus
            cursosTagData = data.cursosTags

            //Como la api nos pide un valor para la creacion del evento una vez recibamos los estados
            // del evento desde la api el mismo viewmodel establecera el primer estado como el predeterminado
            if (eventStatus.isBlank() && data.eventStatus.isNotEmpty() ) {
                eventStatus = data.eventStatus.first()
            }

        } catch (e:Exception) {
            errorMessage = e.message ?: "Error cargar las opciones"
        } finally {
            isLoadingData = false
        }
    }

    var formError by mutableStateOf<String?>(null)
    private set

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