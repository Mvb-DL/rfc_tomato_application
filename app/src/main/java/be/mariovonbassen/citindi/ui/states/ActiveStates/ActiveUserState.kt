package be.mariovonbassen.citindi.ui.states.ActiveStates

import be.mariovonbassen.citindi.models.User

data class ActiveUserState(
    val activeUser: User? = null,
    val isActive: Boolean = false,
)