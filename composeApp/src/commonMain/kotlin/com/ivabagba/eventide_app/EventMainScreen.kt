package com.ivabagba.eventide_app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator

class EventMainScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        Column {
            Text(
                text = "Events",
                style = MaterialTheme.typography.titleLarge
            )
            Button(
                onClick = {
                    navigator?.pop()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver atras")
            }
        }
    }
}