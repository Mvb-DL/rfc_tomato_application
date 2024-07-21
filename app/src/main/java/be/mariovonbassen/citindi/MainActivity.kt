package be.mariovonbassen.citindi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import be.mariovonbassen.citindi.navigation.CitIndiApp
import be.mariovonbassen.citindi.ui.theme.CitIndiTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CitIndiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CitIndiApp()
                }
            }
        }


    }

}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CitIndiTheme {
        CitIndiApp()
    }
}