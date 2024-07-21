package be.mariovonbassen.citindi.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mariovonbassen.citindi.database.events.ChangeAccountEvent
import be.mariovonbassen.citindi.database.repositories.UserRepository
import be.mariovonbassen.citindi.ui.components.ErrorType
import be.mariovonbassen.citindi.ui.states.ActiveStates.ActiveUserState
import be.mariovonbassen.citindi.ui.states.ChangeAccountState
import be.mariovonbassen.citindi.ui.states.ActiveStates.GlobalActiveUserState
import be.mariovonbassen.citindi.ui.states.ChangeAccountErrorState
import be.mariovonbassen.citindi.ui.states.LoginErrorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangeAccountViewModel (
    private val userRepository: UserRepository
) : ViewModel() {

    val globalActiveUserState: StateFlow<ActiveUserState> = GlobalActiveUserState.activeState

    private val _state = MutableStateFlow(ChangeAccountState())
    val state: StateFlow<ChangeAccountState> = _state.asStateFlow()

    private val _errorState = MutableStateFlow(ChangeAccountErrorState())
    val errorState: StateFlow<ChangeAccountErrorState> = _errorState.asStateFlow()

    fun onUserEvent(event: ChangeAccountEvent) {

        when (event) {

            is ChangeAccountEvent.SetNewUserName -> {
                _state.update {
                    it.copy(
                        userName = event.userName
                    )
                }
            }

            is ChangeAccountEvent.SetNewPassword -> {
                _state.update {
                    it.copy(
                        userPassword = event.userPassword
                    )
                }
            }

            is ChangeAccountEvent.ConfirmChangeAccount -> {

                val validatedChangeAccountInputs = validateChangeAccountInputs()

                if (validatedChangeAccountInputs) {

                    val activeUser = globalActiveUserState.value.activeUser

                    if (activeUser != null) {

                        viewModelScope.launch {

                            val user = userRepository.getUserByPasswordAndUserName(
                                activeUser.password,
                                activeUser.userName
                            )

                            val newUserName = state.value.userName
                            val newUserPassword = state.value.userPassword

                            //Check if data is empty and keep old state data
                            if (newUserName.isNotEmpty() || newUserPassword.isNotEmpty()) {

                                user.apply {
                                    userName = newUserName
                                    password = newUserPassword
                                }

                                userRepository.upsertUser(user)

                                //update active User
                                val updatedState =
                                    ActiveUserState(activeUser = user, isActive = true)

                                GlobalActiveUserState.updateAppState(updatedState)

                            }
                        }

                        _state.update {
                            it.copy(
                                isChangeAccountSuccessfull = true
                            )
                        }
                    }
                }
            }
        }
    }

    fun resetError(){
        _errorState.update {
            it.copy(isError = false)
        }
    }

    private fun validateChangeAccountInputs(): Boolean {

        val userName = state.value.userName.trim()
        val userPassword = state.value.userPassword.trim()
        val activeUser = globalActiveUserState.value.activeUser

        return when {

            userName == activeUser?.userName -> {

                if (errorState.value.errorType == ErrorType.CHANGE_ACCOUNT_ERROR) {
                    _errorState.update {
                        it.copy(
                            isError = true,
                            errorMessage = "New Username can´t be like the old one!"
                        )
                    }
                }
                false
            }

            userPassword == activeUser?.password -> {

                if (errorState.value.errorType == ErrorType.CHANGE_ACCOUNT_ERROR) {
                    _errorState.update {
                        it.copy(
                            isError = true,
                            errorMessage = "New Password can´t be like the old one!"
                        )
                    }
                }
                false
            }

            userName.isEmpty() -> {

                if (errorState.value.errorType == ErrorType.CHANGE_ACCOUNT_ERROR) {

                    _errorState.update {
                        it.copy(
                            isError = true,
                            errorMessage = "Username is empty!"
                        )
                    }
                }
                false
            }

            userPassword.isEmpty() -> {
                if (errorState.value.errorType == ErrorType.CHANGE_ACCOUNT_ERROR) {
                    _errorState.update {
                        it.copy(
                            isError = true,
                            errorMessage = "Password is empty!"
                        )
                    }
                }
                false
            }


            else -> {

                _errorState.update {
                    it.copy(
                        isError = false,
                        errorMessage = ""
                    )
                }

                true
            }
        }
    }
}

