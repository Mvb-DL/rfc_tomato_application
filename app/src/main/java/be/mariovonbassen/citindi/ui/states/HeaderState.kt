package be.mariovonbassen.citindi.ui.states


data class HeaderState(
    val currentRoute: String = "",
    val existingRoutes: MutableList<String> = mutableListOf(),
    val activeRoute: String = ""
)