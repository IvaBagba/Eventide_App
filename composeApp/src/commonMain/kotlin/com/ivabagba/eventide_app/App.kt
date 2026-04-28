package com.ivabagba.eventide_app

import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator

@Composable
fun App() {
    Navigator(LoginScreen())
}