package be.mariovonbassen.citindi.ui.states

import be.mariovonbassen.citindi.models.User

data class SettingsState(
    val isLogoutSuccessfull: Boolean = false,
    val isDeleteUserSuccessfull: Boolean = false
)