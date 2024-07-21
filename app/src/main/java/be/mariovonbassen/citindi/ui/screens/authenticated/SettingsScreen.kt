package be.mariovonbassen.citindi.ui.screens.authenticated

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import be.mariovonbassen.citindi.R
import be.mariovonbassen.citindi.database.UserDatabase
import be.mariovonbassen.citindi.database.events.ChangeAccountEvent
import be.mariovonbassen.citindi.database.events.SettingsEvent
import be.mariovonbassen.citindi.database.repositories.OfflineCityRepository
import be.mariovonbassen.citindi.database.repositories.OfflineUserRepository
import be.mariovonbassen.citindi.navigation.NavigationRoutes
import be.mariovonbassen.citindi.ui.BuildRepositories
import be.mariovonbassen.citindi.ui.MainViewModelFactory
import be.mariovonbassen.citindi.ui.components.Header
import be.mariovonbassen.citindi.ui.components.HomeButton
import be.mariovonbassen.citindi.ui.provideProfileViewModel
import be.mariovonbassen.citindi.ui.provideSettingsViewModel
import be.mariovonbassen.citindi.ui.theme.blueAppColor
import be.mariovonbassen.citindi.ui.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(navController: NavController, currentRoute: String,
                   onNavigateToUnauthenticatedRoute: () -> Unit){

    val (userRepository, cityRepository) = BuildRepositories()

    val viewModelFactory = MainViewModelFactory(userRepository, cityRepository)
    val viewmodel = provideSettingsViewModel(viewModelFactory)

    val state by viewmodel.state.collectAsState()

    if (state.isLogoutSuccessfull || state.isDeleteUserSuccessfull) {
        LaunchedEffect(key1 = true) {
            onNavigateToUnauthenticatedRoute.invoke()
        }
    }

    Surface(
        modifier = Modifier
    ) {
        Box{
            Header(navController = navController, currentRoute=currentRoute)
        }

        Box(
            modifier = Modifier
        ) {
            SettingsButton(navController=navController, viewmodel=viewmodel)
        }

    }
}

@Composable
fun SettingsButton(navController: NavController, viewmodel: SettingsViewModel) {

    val color = Color(android.graphics.Color.parseColor(blueAppColor))

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                    navController.navigate(NavigationRoutes.Authenticated.ChangeAccountScreen.route)

                }, modifier = Modifier
                    .width(200.dp),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = color)
            ) {

                Text(text = stringResource(id = R.string.change_account_button))

                Spacer(
                    modifier = Modifier
                        .width(15.dp)
                )

                Icon(
                    Icons.Outlined.Face,
                    contentDescription = "change account",
                    tint = Color.White,
                    modifier = Modifier
                        .size(35.dp)
                )
            }

        }

        Spacer(
            modifier = Modifier
                .height(15.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                    viewmodel.onUserEvent(SettingsEvent.ConfirmDeleteUser)
                }, modifier = Modifier
                    .width(200.dp),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = color)
            ) {

                Text(text = stringResource(id = R.string.delete_account_button))

                Spacer(
                    modifier = Modifier
                        .width(15.dp)
                )

                Icon(
                    Icons.Outlined.Clear,
                    contentDescription = "delete Account",
                    tint = Color.White,
                    modifier = Modifier
                        .size(35.dp)
                )
            }

        }

        Spacer(
            modifier = Modifier
                .height(15.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Button(
                onClick = {
                    viewmodel.onUserEvent(SettingsEvent.LogoutUser)
                }, modifier = Modifier
                    .width(200.dp),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = color)
            ) {

                Text(text = stringResource(id = R.string.logout_button))

                Spacer(
                    modifier = Modifier
                        .width(15.dp)
                )

                Icon(
                    Icons.Outlined.Clear,
                    contentDescription = "logout",
                    tint = Color.White,
                    modifier = Modifier
                        .size(35.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier
                .height(25.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HomeButton(navController = navController)
        }
    }
}
