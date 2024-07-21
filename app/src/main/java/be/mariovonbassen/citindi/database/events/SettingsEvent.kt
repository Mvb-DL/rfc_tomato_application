package be.mariovonbassen.citindi.database.events

sealed interface SettingsEvent{
    object LogoutUser: SettingsEvent
    object ConfirmDeleteUser: SettingsEvent
}