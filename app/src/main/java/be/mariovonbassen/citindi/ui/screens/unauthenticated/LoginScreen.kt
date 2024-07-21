package be.mariovonbassen.citindi.ui.screens.unauthenticated


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.mariovonbassen.citindi.R
import be.mariovonbassen.citindi.database.UserDatabase
import be.mariovonbassen.citindi.database.events.LoginUserEvent
import be.mariovonbassen.citindi.database.repositories.OfflineCityRepository
import be.mariovonbassen.citindi.database.repositories.OfflineUserRepository
import be.mariovonbassen.citindi.ui.BuildRepositories
import be.mariovonbassen.citindi.ui.MainViewModelFactory
import be.mariovonbassen.citindi.ui.components.AlertMessage
import be.mariovonbassen.citindi.ui.provideLoginViewModel
import be.mariovonbassen.citindi.ui.theme.blueAppColor


@Composable
fun LoginScreen(onNavigateToRegistration: () -> Unit,
                onNavigateToAuthenticatedRoute: () -> Unit) {

    val (userRepository, cityRepository) = BuildRepositories()

    val viewModelFactory = MainViewModelFactory(userRepository, cityRepository)
    val viewmodel = provideLoginViewModel(viewModelFactory)

    val state by viewmodel.state.collectAsState()
    val errorstate by viewmodel.errorState.collectAsState()
    val color = Color(android.graphics.Color.parseColor(blueAppColor))

    if (state.isLoginSuccessful) {
        LaunchedEffect(key1 = true) {
            onNavigateToAuthenticatedRoute.invoke()
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        ClickableText(
            text = AnnotatedString(stringResource(id = R.string.sign_up_link)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onClick = { onNavigateToRegistration.invoke() },
            style = TextStyle(
                fontSize = 18.sp,
                textDecoration = TextDecoration.Underline,
            )
        )
    }

    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (errorstate.isError){
            AlertMessage(alertText = errorstate.errorMessage)
        }

        Text(text = stringResource(id = R.string.login_headline), style = TextStyle(fontSize = 40.sp))

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = stringResource(id = R.string.username_textfield)) },
            value = state.userName,
            onValueChange = { viewmodel.onUserEvent(LoginUserEvent.SetUserName(it)) })

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = stringResource(id = R.string.password_textfield)) },
            value = state.userPassword,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { viewmodel.onUserEvent(LoginUserEvent.SetPassword(it)) })

        Spacer(modifier = Modifier.height(25.dp))

        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    viewmodel.onUserEvent(LoginUserEvent.ConfirmLogin)
                },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = color),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(50.dp)

            ) {
                Text(text = stringResource(id = R.string.login_headline))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

    }
}