package be.mariovonbassen.citindi.database.events


sealed interface SignUpUserEvent{
    object SaveUser: SignUpUserEvent
    data class SetUserName(val userName: String): SignUpUserEvent
    data class SetPassword(val userPassword: String): SignUpUserEvent
    data class SetPasswordConfirm(val userPasswordConfirm: String): SignUpUserEvent
    object ConfirmSignUp: SignUpUserEvent
}

