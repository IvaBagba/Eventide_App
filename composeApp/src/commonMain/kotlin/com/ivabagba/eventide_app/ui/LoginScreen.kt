package com.ivabagba.eventide_app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.ivabagba.eventide_app.data.api.CreateHttpClient
import com.ivabagba.eventide_app.data.api.EventApiService
import com.ivabagba.eventide_app.viewModel.LoginVm

class LoginScreen : Screen {

    @Preview
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        val client = remember { CreateHttpClient() }
        val api = remember { EventApiService(client) }
        val viewModel = remember { LoginVm(api) }

        //variables de estado
        var showPassword by remember { mutableStateOf(false) }
        var loginVisible by remember { mutableStateOf(false) }

        //Cambia el estado de login visible a true cuando se dibuja la pantalla por PRIMERA VEZ
        LaunchedEffect(Unit) {
            loginVisible = true
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(32.dp),
            contentAlignment = Alignment.Center,
        ) {
            //Animacion de el elemento login
            AnimatedVisibility(
                //Parametros de la animacion
                visible = loginVisible,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                ) + scaleIn(
                    initialScale = 0.9f,
                    animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing)
                ) + slideInVertically(
                    initialOffsetY = { it / 4 },
                )
            ) {
                Card(
                    modifier = Modifier
                        .widthIn(max = 420.dp)
                        .fillMaxWidth()
                        .heightIn(max = 600.dp)
                        .animateContentSize(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    elevation = CardDefaults.cardElevation(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(28.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Eventide",
                            style = MaterialTheme.typography.headlineLarge,
                        )
                        Text(
                            text = "Accede a tu cuenta para ver los eventos",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        OutlinedTextField(
                            value = viewModel.dni,
                            onValueChange = { viewModel.dni = it },
                            label = { Text("DNI") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()

                        )

                        OutlinedTextField(
                            value = viewModel.userPass,
                            onValueChange = { viewModel.userPass = it },
                            label = { Text("Contraseña") },
                            singleLine = true,

                            //funcion mostrar contraseña
                            visualTransformation = if (showPassword) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },

                            trailingIcon = {
                                IconButton(onClick = { showPassword = !showPassword }) {
                                    Icon(
                                        imageVector = if (showPassword)
                                            Icons.Default.Visibility
                                        else
                                            Icons.Default.VisibilityOff,
                                        contentDescription = "Mostrar contraseña"
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = {
                                viewModel.onLogin{ loggedIn ->
                                    navigator?.push(EventMainScreen(loggedIn))
                                }
                            },
                            enabled = !viewModel.isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                        ) {
                            Text(if (!viewModel.isLoading) "Iniciar Sesión" else "Iniciando Sesión")
                        }

                        viewModel.errorMessage?.let {
                            errorMessage ->
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }

                }
            }
        }
    }
}


