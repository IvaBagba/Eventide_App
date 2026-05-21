package com.ivabagba.eventide_app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

@Composable
fun EventDatePicker(
    value: String,
    onValueChange: (String) -> Unit
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showDatePickerDialog = true
            }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = {Text("Fecha")},
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledContainerColor = Color.Transparent,
            )
        )
    }

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePickerDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedMilis = datePickerState.selectedDateMillis

                        if (selectedMilis != null) {
                            val selectedDate = millisToDateString(selectedMilis)
                            onValueChange(selectedDate)
                        }

                        showDatePickerDialog = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePickerDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
            )
        }
    }
}

fun millisToDateString(selectedMilis: Long) : String {
    val instant = Instant.fromEpochMilliseconds(selectedMilis)
    val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    return date.toString()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTimePicker(
    value: String,
    onValueChange: (String) -> Unit,
) {
    var showTimePickerDialog by remember { mutableStateOf(false) }

    val timeParts = value.split(":")

    val initialHour = timeParts.getOrNull(0)?.toIntOrNull() ?: 12
    val initialMinute = timeParts.getOrNull(1)?.toIntOrNull() ?: 0

    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showTimePickerDialog = true
            }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = {Text("Hora")},
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledContainerColor = Color.Transparent,
            )
        )
    }

    if (showTimePickerDialog) {
        AlertDialog(
            onDismissRequest = {
                showTimePickerDialog = false
            },
            title = {
                Text("Seleccionar Hora")
            },
            text = {
                TimePicker(
                    state = timePickerState,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val hour = timePickerState.hour
                            .toString()
                            .padStart(2, '0')

                        val minute = timePickerState.minute
                            .toString()
                            .padStart(2, '0')

                        onValueChange("$hour:$minute:00")
                        showTimePickerDialog = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showTimePickerDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}