package be.mariovonbassen.citindi.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import be.mariovonbassen.citindi.navigation.NavigationRoutes
import be.mariovonbassen.citindi.ui.theme.blueAppColor

@Composable
fun Footer(navController: NavController) {

    val color = Color(android.graphics.Color.parseColor(blueAppColor))

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = Color.White,
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

                IconButton(onClick = {
                    navController.navigate(NavigationRoutes.Authenticated.AddCityScreen.route)
                }) {

                    Icon(
                        Icons.Rounded.AddCircle,
                        contentDescription = "add city",
                        tint  = color,
                        modifier = Modifier
                            .size(40.dp)
                    )
                }

            IconButton(onClick = {
                navController.navigate(NavigationRoutes.Authenticated.SettingsScreen.route)

            }) {

                Column {
                    Icon(
                        Icons.Rounded.Settings,
                        contentDescription = "settings",
                        tint  = color,
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
            }

            IconButton(onClick = {
                navController.navigate(NavigationRoutes.Authenticated.ProfileScreen.route)
            }) {
                Icon(
                    Icons.Rounded.AccountCircle,
                    contentDescription = "profile",
                    tint  = color,
                    modifier = Modifier
                        .size(40.dp)
                )
            }
        }
    }
}