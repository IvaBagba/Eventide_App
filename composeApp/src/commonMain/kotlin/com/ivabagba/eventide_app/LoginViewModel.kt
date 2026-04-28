package com.ivabagba.eventide_app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class LoginViewModel {

    var user by mutableStateOf("")
    var password by mutableStateOf("")

    fun onUserTChange(userT: String) {
        this.user = userT
    }

    fun onPasswordTChange(passwordT: String) {
        this.password = passwordT
    }

    fun onLogin(): Boolean {
        return user.isNotBlank() && password.isNotBlank()
    }
}