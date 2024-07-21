package be.mariovonbassen.citindi.ui.screens.authenticated

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import be.mariovonbassen.citindi.database.UserDatabase
import be.mariovonbassen.citindi.database.events.ChangeAccountEvent
import be.mariovonbassen.citindi.database.events.LoginUserEvent
import be.mariovonbassen.citindi.database.events.SignUpUserEvent
import be.mariovonbassen.citindi.database.repositories.OfflineCityRepository
import be.mariovonbassen.citindi.database.repositories.OfflineUserRepository
import be.mariovonbassen.citindi.navigation.NavigationRoutes
import be.mariovonbassen.citindi.ui.BuildRepositories
import be.mariovonbassen.citindi.ui.MainViewModelFactory
import be.mariovonbassen.citindi.ui.components.AlertMessage
import be.mariovonbassen.citindi.ui.components.Header
import be.mariovonbassen.citindi.ui.provideChangeAccountViewModel
import be.mariovonbassen.citindi.ui.provideProfileViewModel
import be.mariovonbassen.citindi.ui.theme.blueAppColor

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ChangeAccountScreen(navController: NavController,
                        currentRoute: String,
                        onNavigateToAuthenticatedRoute: () -> Unit){

    val (userRepository, cityRepository) = BuildRepositories()

    val viewModelFactory = MainViewModelFactory(userRepository, cityRepository)
    val viewmodel = provideChangeAccountViewModel(viewModelFactory)

    val state by viewmodel.state.collectAsState()
    val errorstate by viewmodel.errorState.collectAsState()
    val color = Color(android.graphics.Color.parseColor(blueAppColor))

    if (state.isChangeAccountSuccessfull) {
        LaunchedEffect(key1 = true) {
            onNavigateToAuthenticatedRoute.invoke()
        }
    }

    Surface(
        modifier = Modifier
    ) {
        Box {
            Header(navController = navController, currentRoute = currentRoute)
        }

        Column(
            modifier = Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Change Account", style = TextStyle(fontSize = 40.sp))

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                label = { Text(text = "Enter new Username") },
                value = state.userName,
                onValueChange = {
                    viewmodel.onUserEvent(ChangeAccountEvent.SetNewUserName(it))
                })

            if (errorstate.isError){
                AlertMessage(alertText = errorstate.errorMessage)
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                label = { Text(text = "Enter new Password") },
                value = state.userPassword,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = {
                    viewmodel.onUserEvent(ChangeAccountEvent.SetNewPassword(it))
                })

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewmodel.onUserEvent(ChangeAccountEvent.ConfirmChangeAccount)

                },
                colors = ButtonDefaults.buttonColors(containerColor = color),
            ){
                Text(text = "Change Account")
            }

        }

    }

}