package be.mariovonbassen.citindi.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mariovonbassen.citindi.database.events.LoginUserEvent
import be.mariovonbassen.citindi.database.repositories.CityRepository
import be.mariovonbassen.citindi.database.repositories.UserRepository
import be.mariovonbassen.citindi.ui.components.ErrorType
import be.mariovonbassen.citindi.ui.states.ActiveStates.ActiveCityState
import be.mariovonbassen.citindi.ui.states.ActiveStates.ActiveUserState
import be.mariovonbassen.citindi.ui.states.ActiveStates.GlobalActiveCityState
import be.mariovonbassen.citindi.ui.states.ActiveStates.GlobalActiveUserState
import be.mariovonbassen.citindi.ui.states.LoginErrorState
import be.mariovonbassen.citindi.ui.states.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
    private val cityRepository: CityRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _errorState = MutableStateFlow(LoginErrorState())
    val errorState: StateFlow<LoginErrorState> = _errorState.asStateFlow()

    //handling the active logged in User
    fun onUserEvent(event: LoginUserEvent) {

        when (event) {

            is LoginUserEvent.SetUserName -> {
                _state.update {
                    it.copy(
                        userName = event.userName
                    )
                }
            }

            is LoginUserEvent.SetPassword -> {
                _state.update {
                    it.copy(
                        userPassword = event.userPassword
                    )
                }
            }

            is LoginUserEvent.ConfirmLogin -> {

                val inputsValidated = validateLoginInputs()

                if (inputsValidated) {

                        viewModelScope.launch {

                            val userExist = userRepository.checkUserPresence(
                                state.value.userPassword,
                                state.value.userName)

                            if (userExist) {

                                //check active user
                                val activeUser = userRepository.getUserByPasswordAndUserName(
                                    state.value.userPassword,
                                    state.value.userName)

                                val updatedUserState = ActiveUserState(activeUser= activeUser, isActive = true)

                                GlobalActiveUserState.updateAppState(updatedUserState)

                                val latestCity = updatedUserState.activeUser?.let {
                                    cityRepository.getLatestCity(
                                        it.userId)
                                }

                                val updatedCityState = ActiveCityState(activeCity = latestCity, isActive = true)

                                GlobalActiveCityState.updateCityAppState(updatedCityState)

                                _state.update {
                                    it.copy(isLoginSuccessful = true)
                                }

                            }else {

                                _state.update {
                                    it.copy(isLoginSuccessful = false)
                                }

                                _errorState.update {
                                    it.copy(
                                        isError = true,
                                        errorMessage = "Password or Username wrong!"
                                    )
                                }
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

    private fun validateLoginInputs(): Boolean {

        val userName = state.value.userName.trim()
        val userPassword = state.value.userPassword.trim()

        return when {

            userName.isEmpty() -> {
                if (errorState.value.errorType == ErrorType.LOGIN_ERROR)
                {
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
                if (errorState.value.errorType == ErrorType.LOGIN_ERROR)
                {
                    _errorState.update {
                        it.copy(
                            isError = true,
                            errorMessage = "Password is empty!"
                        )
                    }
                }
                false
            }

            // No errors
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