package be.mariovonbassen.citindi.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import be.mariovonbassen.citindi.R
import be.mariovonbassen.citindi.navigation.NavigationRoutes

@Composable
fun HomeButton(navController: NavController){

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        Row {
            IconButton(onClick = {
                navController.navigate(NavigationRoutes.Authenticated.MainDashBoardScreen.route)
            }) {
                Icon(
                    Icons.Outlined.Home,
                    contentDescription = "home button",
                    tint  = Color.Black,
                    modifier = Modifier
                        .size(35.dp)
                )
            }
        }
        
        Row {
            Text(text = stringResource(id = R.string.back_home_button), fontSize = 15.sp)
        }

        
    }
}
