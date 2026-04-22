package com.ivabagba.eventide_app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "eventide_app",
    ) {
        App()
    }
}