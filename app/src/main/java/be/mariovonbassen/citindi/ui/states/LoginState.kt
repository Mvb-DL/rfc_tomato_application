package be.mariovonbassen.citindi.ui.states

import be.mariovonbassen.citindi.models.User
import be.mariovonbassen.citindi.ui.components.ErrorType

data class LoginState(
    val users: List<User> = emptyList(),
    val userName: String = "",
    val userPassword : String = "",
    val isLoginSuccessful: Boolean = false
)

data class LoginErrorState(
    val isError: Boolean = false,
    val errorType: ErrorType = ErrorType.LOGIN_ERROR,
    val errorMessage: String = "",
)