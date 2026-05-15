package com.ivabagba.eventide_app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.ivabagba.eventide_app.data.api.EventApiService
import com.ivabagba.eventide_app.data.api.CreateHttpClient
import com.ivabagba.eventide_app.data.dto.EventResponseDto
import com.ivabagba.eventide_app.data.dto.login.LoginResDto
import com.ivabagba.eventide_app.viewModel.EventMainVm
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class EventMainScreen(
    private val loggedIn: LoginResDto
) : Screen {

    @Preview
    @Composable
    override fun Content() {
        val client = remember { CreateHttpClient() }
        val api = remember { EventApiService(client) }
        val viewModel = remember { EventMainVm(api) }

        val navigator = LocalNavigator.current

        val isAdmin = loggedIn.userRole == "ADMIN"

        //Variable que guarda que evento hemos seleccionado para ver su detalle, puede ser nulo
        var selectEvent by remember { mutableStateOf<EventResponseDto?>(null) }
        //Variable que nos dice si mostramos el detalle del evento
        var showEventDetail by remember { mutableStateOf(false) }

        var showDeleteConfirmation by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            viewModel.loadEvents()
        }

        //Cuando se cierra el detalle del evento también borramos los datos de que detalle de evento estábamos viendo
        LaunchedEffect(showEventDetail) {
            if (!showEventDetail) {
                delay(180.milliseconds)
                selectEvent = null
            }
        }

        Scaffold (
            containerColor = MaterialTheme.colorScheme.background,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navigator?.push(EventCreateScreen())
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Crear Evento"
                    )
                }
            }
        ) { paddingValues ->

            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(20.dp),
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ){
                        Column {
                            Text(
                                text = "Bienvenido",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )

                            Text(
                                text = loggedIn.userName,
                                style = MaterialTheme.typography.bodySmall,
                            )

                            Text(
                                text = loggedIn.userRole,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                            )

                        }
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = MaterialTheme.shapes.large,
                            color = MaterialTheme.colorScheme.surface,
                        ){
                            IconButton(onClick = {
                                navigator?.pop()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }

                        }

                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                    )

                    Text(
                        text = "Eventos",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                    )



                    Spacer(modifier = Modifier.height(16.dp))

                    //Aquí se muestra el estado del listado de eventos, es decir el estado de carga , error o en su defecto el listado con los eventos
                    when {
                        viewModel.isLoading -> {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        viewModel.errorMessage != null -> {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = viewModel.errorMessage!!,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }

                        else -> {
                            //Grid donde están ubicados los eventos "Lazy hace que los eventos fuera de vista no se renderizen"
                            LazyVerticalGrid(
                                //tamaño minimo de cada "evento"
                                columns = GridCells.Adaptive(180.dp),
                                modifier = Modifier.fillMaxSize(),
                                //separación entre eventos
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                            ) {
                                //generamos/dibujamos nuestros eventos que son un composable diferente
                                items(viewModel.events) { event ->
                                    EventCard(
                                        event = event,
                                        onClick = { selectEvent = event
                                                  showEventDetail = true},)
                                }
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = showEventDetail,
                    enter = fadeIn(animationSpec = tween(220)) + scaleIn(animationSpec = tween(220), initialScale = 0.96f),
                    exit = fadeOut(animationSpec = tween(180)) + scaleOut(animationSpec = tween(180), targetScale = 0.96f),
                ) {
                    //Apertura del overlay del evento en detalle
                    selectEvent?.let { event ->
                        EventDetailScreen(
                            event = event,
                            isDeleting = viewModel.isDeleting,
                            deleteError = viewModel.deleterError,
                            onDismiss = {
                                if (!viewModel.isDeleting) {
                                    showEventDetail = false
                                }

                            },
                            onDeleteClick = {
                                showDeleteConfirmation = true
                            },

                            onEditClick = {
                                navigator?.push(EventEditScreen(event))
                            }
                        )
                    }
                }
            }

            if (showDeleteConfirmation && selectEvent != null) {
                AlertDialog(
                    onDismissRequest = {
                        if (viewModel.isDeleting) {
                            showDeleteConfirmation = false
                        }
                    },
                    title = {
                        Text(text = "Eliminar Evento")
                    },
                    text = {
                        Text(text = "Seguro que quieres eliminar este evento?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                selectEvent?.id?.let { id ->
                                    viewModel.deleteEvent(
                                        id,
                                        onSuccess = {
                                            showEventDetail = false
                                            showDeleteConfirmation = false
                                            selectEvent = null
                                        }
                                    )
                                }
                            },
                            enabled = !viewModel.isDeleting,
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.onError,
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(
                                if (viewModel.isDeleting) {
                                    "Eliminando evento...."
                                } else {
                                    "Eliminar"
                                }
                            )
                        }
                    },
                    dismissButton = {
                        OutlinedButton(
                            onClick = {
                                showDeleteConfirmation = false
                            },
                            enabled = !viewModel.isDeleting,
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
            }

        }

    }

    //Vista de evento en detalle (Solo uno y toda la pantalla)
    @Composable
    fun EventDetailScreen(
        event: EventResponseDto,
        onDismiss: () -> Unit,
        onDeleteClick: () -> Unit,
        onEditClick: () -> Unit,
        isDeleting: Boolean,
        deleteError: String?,

    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickable (
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) {onDismiss()},
            contentAlignment = Alignment.Center
        ){
            Card(
                modifier = Modifier
                .fillMaxWidth(0.90f)
                .fillMaxHeight(0.80f)
                    .pointerInput(Unit) {
                        detectTapGestures {  }
                    },
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp,
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),

                ) {
                    Text(
                        text = event.eventName,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )


                    Text(
                        text = event.eventDesc ?: "Sin descripcion",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                    HorizontalDivider()

                    Text(
                        text = "Fecha: " + event.eventDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = "Hora: " + event.eventDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Text(
                        text = "Ubicación: " + event.eventLocation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = "Estado: " + event.eventStatus,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    deleteError?.let {error ->
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                            OutlinedButton(
                            onClick = onDismiss,
                            enabled = !isDeleting,
                        ) {
                            Text(text = "Cerrar")
                        }

                        Spacer(modifier = Modifier.size(12.dp))
                        Button(
                            onClick = onEditClick,
                            enabled = !isDeleting,
                        ) {
                            Text(text = "Editar")
                        }

                        Spacer(modifier = Modifier.size(12.dp))
                        Button(
                            onClick = onDeleteClick,
                            enabled = !isDeleting,
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.onError,
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(text = "Eliminar")
                        }
                    }

                }
            }
        }
    }


    @Composable
    fun EventCard(event: EventResponseDto,
                  onClick: () -> Unit,) {
        Card (
            onClick = onClick,
            modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
            //forma de la tarjeta
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                //Color de fondo de cada tarjeta
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            //elevacion / sombra de cada tarjeta
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp,
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = event.eventName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = event.eventDesc ?: "Sin descripcion",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = event.eventDate + " - " + event.eventTime,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )

                Text(
                    text = event.eventLocation,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

            }
        }
    }
}