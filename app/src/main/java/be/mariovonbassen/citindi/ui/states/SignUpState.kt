package be.mariovonbassen.citindi.ui.states


import be.mariovonbassen.citindi.models.User
import be.mariovonbassen.citindi.ui.components.ErrorType
import kotlinx.coroutines.flow.Flow

data class SignUpState(
    val users: List<User> = emptyList(),
    val userName: String = "",
    val userPassword: String = "",
    val userPasswordConfirm: String = "",
    val errorState: RegistrationErrorState = RegistrationErrorState(),
    val isRegistrationSuccessful: Boolean = false,
)

data class RegistrationErrorState(
    val isError: Boolean = false,
    val errorType: ErrorType = ErrorType.REGISTRATION_ERROR,
    val errorMessage: String = "",
)