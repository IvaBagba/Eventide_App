package com.ivabagba.eventide_app.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class LoginVm {

    var user by mutableStateOf("")
    var password by mutableStateOf("")

    fun onUserTChange(userT: String) {
        this.user = userT
    }

    fun onPasswordTChange(passwordT: String) {
        this.password = passwordT
    }

    fun onLogin(): Boolean {
        return user == "admin" && password == "1234"
    }
}