package be.mariovonbassen.citindi.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import be.mariovonbassen.citindi.navigation.Navigation
import be.mariovonbassen.citindi.navigation.NavigationRoutes
import be.mariovonbassen.citindi.ui.states.AddCityState
import be.mariovonbassen.citindi.ui.theme.blueAppColor
import be.mariovonbassen.citindi.ui.viewmodels.AddCityViewModel
import be.mariovonbassen.citindi.ui.viewmodels.HeaderViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun Header(viewmodel : HeaderViewModel = viewModel(), navController: NavController, currentRoute: String) {

    val state by viewmodel.state.collectAsState()

    viewmodel.setCurrentRouteName(currentRoute)
    viewmodel.getAllRoutes(navController=navController, currentRoute=currentRoute)


    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 5.dp, 5.dp, 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        val color = Color(android.graphics.Color.parseColor(blueAppColor))

        DropDownMenu(navController=navController, existingRoutes = state.existingRoutes)

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = state.currentRoute, fontSize = 25.sp)

        Spacer(modifier = Modifier.height(20.dp))

        IconButton(onClick = {
            navController.navigate(NavigationRoutes.Authenticated.SettingsScreen.route)

        }) {

            Column {

                if (currentRoute != "Settings") {

                    Icon(
                        Icons.Rounded.Settings,
                        contentDescription = "settings",
                        tint = color,
                        modifier = Modifier
                            .size(40.dp)
                    )

                }
            }
        }

    }
}


