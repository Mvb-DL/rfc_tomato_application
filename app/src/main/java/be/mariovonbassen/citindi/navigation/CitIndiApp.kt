package be.mariovonbassen.citindi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


@Composable
fun Navigation(){

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Unauthenticated.NavigationRoute.route,
    ) {

        unauthenticatedGraph(navController = navController)

        authenticatedGraph(navController = navController, currentRoute = currentRoute)
    }

}

@Composable
fun CitIndiApp() {
    Navigation()
}