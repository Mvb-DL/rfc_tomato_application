package be.mariovonbassen.citindi.database.events


sealed interface LoginUserEvent{
    data class SetUserName(val userName: String): LoginUserEvent
    data class SetPassword(val userPassword: String): LoginUserEvent
    object ConfirmLogin: LoginUserEvent
}