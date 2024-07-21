package be.mariovonbassen.citindi.database.events

sealed interface ChangeAccountEvent{
    data class SetNewUserName(val userName: String): ChangeAccountEvent
    data class SetNewPassword(val userPassword: String): ChangeAccountEvent
    object ConfirmChangeAccount: ChangeAccountEvent
}