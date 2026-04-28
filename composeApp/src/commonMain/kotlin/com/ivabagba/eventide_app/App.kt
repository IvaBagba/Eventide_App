package com.ivabagba.eventide_app

import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import com.ivabagba.eventide_app.ui.LoginScreen
import com.ivabagba.eventide_app.ui.themes.AppTheme

@Composable
fun App() {
    AppTheme {
        Navigator(LoginScreen())
    }

}