package be.mariovonbassen.citindi.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mariovonbassen.citindi.database.events.SettingsEvent
import be.mariovonbassen.citindi.database.repositories.UserRepository
import be.mariovonbassen.citindi.ui.states.ActiveStates.ActiveCityState
import be.mariovonbassen.citindi.ui.states.ActiveStates.ActiveUserState
import be.mariovonbassen.citindi.ui.states.ActiveStates.GlobalActiveCityState
import be.mariovonbassen.citindi.ui.states.ActiveStates.GlobalActiveUserState
import be.mariovonbassen.citindi.ui.states.LoginState
import be.mariovonbassen.citindi.ui.states.SettingsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    private var globalActiveUserState: StateFlow<ActiveUserState>? = GlobalActiveUserState.activeState
    private var globalActiveCityState: StateFlow<ActiveCityState>? = GlobalActiveCityState.activeState

    fun onUserEvent(event: SettingsEvent) {

        when (event) {

            is SettingsEvent.ConfirmDeleteUser -> {

                viewModelScope.launch {

                    if (globalActiveUserState?.value?.activeUser != null) {

                        withContext(Dispatchers.IO) {

                            val user = userRepository.getUser(globalActiveUserState?.value?.activeUser!!.userId)

                            userRepository.deleteUser(user)

                            _state.update {
                                it.copy(
                                    isDeleteUserSuccessfull = true
                                )
                            }
                        }

                    }else{
                        Log.d("User is Null", "")
                    }

                }

            }

            is SettingsEvent.LogoutUser -> {

                globalActiveUserState = null
                globalActiveCityState = null

                _state.update {
                    it.copy(
                        isLogoutSuccessfull = true
                    )
                }

            }

        }
    }

}