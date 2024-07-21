package be.mariovonbassen.citindi.navigation

sealed class NavigationRoutes{

    sealed class Authenticated(val route: String) : NavigationRoutes(){
        object NavigationRoute : Authenticated(route = "authenticated")
        object MainDashBoardScreen : Authenticated("Home")
        object AddCityScreen : Authenticated("Add")
        object ProfileScreen : Authenticated("Profile")
        object SettingsScreen : Authenticated("Settings")
        object ChangeAccountScreen : Authenticated("ChangeAccount")

    }

    sealed class Unauthenticated(val route: String) : NavigationRoutes(){
        object NavigationRoute : Unauthenticated(route = "unauthenticated")
        object LoginScreen : Unauthenticated("login_screen")
        object SignUpScreen : Unauthenticated("signup_screen")

    }


}