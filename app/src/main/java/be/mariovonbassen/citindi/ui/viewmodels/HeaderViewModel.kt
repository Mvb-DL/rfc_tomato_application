package be.mariovonbassen.citindi.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import be.mariovonbassen.citindi.ui.states.HeaderState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HeaderViewModel : ViewModel() {

    private val _state = MutableStateFlow(HeaderState())
    val state: StateFlow<HeaderState> = _state.asStateFlow()

    fun setCurrentRouteName(currentRoute: String){

        if (currentRoute == "Add"){
            _state.update {
                it.copy(currentRoute = "Add City")
            }
        }

        if(currentRoute == "Profile"){
            _state.update {
                it.copy(currentRoute = "Profile")
            }
        }

        if(currentRoute == "Settings"){
            _state.update {
                it.copy(currentRoute = "Settings")
            }
        }
    }

    fun getAllRoutes(navController: NavController, currentRoute: String) {

        val navGraph = navController.graph
        val routeNames = mutableListOf<String>()

        navGraph.forEach { navDestination ->
            if (navDestination.route != currentRoute) {
                routeNames.add(navDestination.route ?: "")
            }
        }

        _state.update {
            it.copy(existingRoutes = routeNames)
        }
    }

}