package com.ivabagba.eventide_app.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ivabagba.eventide_app.data.api.EventApiService
import com.ivabagba.eventide_app.data.dto.login.LoginReqDto
import com.ivabagba.eventide_app.data.dto.login.LoginResDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LoginVm(
    private val apiService: EventApiService
) {

    var dni by mutableStateOf("")
    var userPass by mutableStateOf("")


    var isLoading by mutableStateOf(false)
    private set

    var errorMessage by mutableStateOf<String?>(null)
    private set

    var loggedIn by mutableStateOf<LoginResDto?>(null)
    private set

    private val corroutine = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun onUserTChange(userT: String) {
        this.dni = userT
    }

    fun onPasswordTChange(passwordT: String) {
        this.userPass = passwordT
    }

    fun onLogin(onSuccess: (LoginResDto) -> Unit) {
        if (dni.isBlank()) {
            errorMessage = "Introduce el DNI"
            return
        }

        if (userPass.isBlank()) {
            errorMessage = "Introduce una contraseña"
            return
        }

        corroutine.launch {
            try {
                isLoading = true
                errorMessage = null

                val response = apiService.login(
                    LoginReqDto(
                        dni = dni,
                        userPass = userPass,

                    )
                )

                loggedIn = response
                onSuccess(response)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Error al iniciar sesión, comprueba el usuario o la contraseña"
            } finally {
                isLoading = false
            }
        }
    }
}