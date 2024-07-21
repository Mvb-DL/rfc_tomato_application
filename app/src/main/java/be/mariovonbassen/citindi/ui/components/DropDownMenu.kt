package be.mariovonbassen.citindi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import be.mariovonbassen.citindi.navigation.NavigationRoutes
import be.mariovonbassen.citindi.ui.theme.blueAppColor

@Composable
fun DropDownMenu(existingRoutes: List<String>, navController: NavController) {

    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }
    val color = Color(android.graphics.Color.parseColor(blueAppColor))

    IconButton(onClick = {
        expanded = true
    }) {

        Column {
            Icon(
                Icons.Rounded.Menu,
                contentDescription = "dropdown",
                tint = color,
                modifier = Modifier
                    .size(40.dp)
            )
        }
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier
            .background(
            color
        )
    ) {
        existingRoutes.forEachIndexed { index, route ->

            DropdownMenuItem(onClick = {
                selectedIndex = index
                expanded = false
                navController.navigate(route)
            },
                text = { DropDownItem(route = route) })
        }
    }
}



@Composable
fun DropDownItem(route: String){

    if (route == "unauthenticated") {
        Text(text = "Logout")
    }else{
        Text(text = "Home")
    }
}




