package com.ivabagba.eventide_app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.savedstate.savedState
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.ivabagba.eventide_app.data.api.CreateHttpClient
import com.ivabagba.eventide_app.data.api.EventApiService
import com.ivabagba.eventide_app.viewModel.EventCreationVm
import com.ivabagba.eventide_app.viewModel.EventMainVm
import io.ktor.sse.COLON

class EventCreateScreen : Screen{
    @Preview
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val client = remember { CreateHttpClient() }
        val api = remember { EventApiService(client) }
        val viewModel = remember { EventCreationVm(api) }

        var statusFieldExpanded by remember { mutableStateOf(false) }
        var cursosFieldExpanded by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            viewModel.loadEventDataFields()
        }

        LaunchedEffect(viewModel.isSuccess) {
            if (viewModel.isSuccess) {
                navigator?.pop()
            }
        }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Text(
                    text = "Crear Evento",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                OutlinedTextField(
                    value = viewModel.eventName,
                    onValueChange = { viewModel.eventName = it },
                    label = {Text(" Nombre del evento")},
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = viewModel.eventDesc,
                    onValueChange = { viewModel.eventDesc = it },
                    label = {Text(" Descripcion")},
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 5,
                )

                OutlinedTextField(
                    value = viewModel.eventDate,
                    onValueChange = { viewModel.eventDate = it },
                    label = {Text(" Fecha (yyyy-MM-dd)")},
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = viewModel.eventTime,
                    onValueChange = { viewModel.eventTime = it },
                    label = {Text(" Hora (HH:MM:SS)")},
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = viewModel.eventLocation,
                    onValueChange = { viewModel.eventLocation = it },
                    label = {Text(" Ubicacion")},
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) { OutlinedTextField(
                    value = viewModel.eventStatus,
                    onValueChange = { },
                    readOnly = true,
                    enabled = viewModel.statusData.isNotEmpty(),
                    label = {Text(" Estado")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (viewModel.statusData.isNotEmpty()) {
                                statusFieldExpanded = true
                            }
                        },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (viewModel.statusData.isNotEmpty()) {
                                    statusFieldExpanded = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Abrir Selector de estado",
                            )
                        }
                    }
                )
                    DropdownMenu(
                        expanded = statusFieldExpanded,
                        onDismissRequest = { statusFieldExpanded = false },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        viewModel.statusData.forEach { item ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = item)
                                },
                                onClick = {
                                    viewModel.eventStatus = item
                                    statusFieldExpanded = false
                                }
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    OutlinedTextField(
                        value = if (viewModel.cursosTags.isEmpty()) {
                            ""
                        } else {
                            viewModel.cursosTags.joinToString(", ")
                        },
                        onValueChange = {},
                        readOnly = true,
                        enabled = viewModel.cursosTagData.isNotEmpty(),
                        label = { Text(" Cursos")},
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (viewModel.cursosTagData.isNotEmpty()) {
                                    cursosFieldExpanded = true
                                }
                            },
                        trailingIcon = {
                                IconButton(
                                    onClick = {
                                        if (viewModel.cursosTagData.isNotEmpty()) {
                                            cursosFieldExpanded = true
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Abrir Selector de cursos",
                                    )
                                }
                        }
                    )

                    DropdownMenu(
                        expanded = cursosFieldExpanded,
                        onDismissRequest = { cursosFieldExpanded = false },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        viewModel.cursosTagData.forEach { item ->

                            val isSelected = viewModel.cursosTags.contains(item)

                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    ) {
                                        Checkbox(
                                            checked = isSelected,
                                            onCheckedChange = null
                                        )

                                        Text(text = item)
                                    }
                                },
                                onClick = {
                                    viewModel.cursosTags = if (isSelected) {
                                        viewModel.cursosTags - item
                                    } else {
                                        viewModel.cursosTags + item
                                    }
                                }
                            )
                        }
                    }

                }

                viewModel.errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                viewModel.formError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(
                        onClick = {
                            navigator?.pop()
                        }
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            viewModel.createEvent()
                        },
                        enabled = !viewModel.isSaving,
                    ) {
                        if (viewModel.isSaving) {
                            Text("Guardando...")
                        } else {
                            Text("Guardar evento")
                        }
                    }
                }
                

            }
        }
    }
}