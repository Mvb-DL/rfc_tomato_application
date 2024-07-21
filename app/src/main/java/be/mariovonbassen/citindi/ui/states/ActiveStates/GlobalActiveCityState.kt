package be.mariovonbassen.citindi.ui.states.ActiveStates

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object GlobalActiveCityState {

    private val _activeState = MutableStateFlow(ActiveCityState())
    val activeState: StateFlow<ActiveCityState> = _activeState

    fun updateCityAppState(newState: ActiveCityState) {
        _activeState.value = newState
    }
}
