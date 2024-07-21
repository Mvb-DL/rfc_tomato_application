package be.mariovonbassen.citindi.ui.states.ActiveStates


import be.mariovonbassen.citindi.models.city.City

data class ActiveCityState (
    val activeCity: City? = null,
    val isActive: Boolean = false,
)