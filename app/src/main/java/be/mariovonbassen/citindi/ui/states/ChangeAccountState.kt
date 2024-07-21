package be.mariovonbassen.citindi.ui.states

import be.mariovonbassen.citindi.ui.components.ErrorType

data class ChangeAccountState (
    val userName: String = "",
    val userPassword : String = "",
    val isChangeAccountSuccessfull : Boolean = false
)

data class ChangeAccountErrorState(
    val isError: Boolean = false,
    val errorType: ErrorType = ErrorType.CHANGE_ACCOUNT_ERROR,
    val errorMessage: String = "",
)