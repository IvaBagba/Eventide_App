package com.ivabagba.eventide_app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.ivabagba.eventide_app.viewModel.EventMainVm

class EventMainScreen : Screen {

    @Preview
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        val viewModel = remember { EventMainVm() }

        Scaffold (
            containerColor = MaterialTheme.colorScheme.background,
        ) { paddingValues ->

            Column(
                modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            ) {
                Row (
                    modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    Column {
                        Text(
                            text = "Bienvenido",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                        Text(
                            text = "Usuario",
                            style = MaterialTheme.typography.bodySmall,
                        )

                    }
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.surface,
                    ){
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                )

                Text(
                    text = "Eventos",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Spacer(modifier = Modifier.height(16.dp))

                //Grid donde estan ubicados los eventos "Lazy hace que los eventos fuera de vista no se renderizen"
                LazyVerticalGrid(
                    //tamaño minimo de cada "evento"
                    columns = GridCells.Adaptive(180.dp),
                    modifier = Modifier.fillMaxSize(),
                    //separación entre eventos
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(60){
                        //generamos/dibujamos nuestros eventos que son un composable diferente
                        EventCard()
                    }
                }
            }
        }

    }

    @Composable
    fun EventCard() {
        Card (
            modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
            //forma de la tarjeta
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                //Color de fondo de cada tarjeta
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            //elevacion / sombra de cada tarjeta
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp,
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Test Tarjeta evento",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = "Descripcion de ejempplo",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "xx/xx --- xx:xx",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )

                Text(
                    text = "Lugar de ejempplo",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}