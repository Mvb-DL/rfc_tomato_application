package be.mariovonbassen.citindi.ui.screens.authenticated

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import be.mariovonbassen.citindi.R
import be.mariovonbassen.citindi.database.events.MainDashBoardEvent
import be.mariovonbassen.citindi.models.city.CitySentence
import be.mariovonbassen.citindi.models.city.CitySightSeeing
import be.mariovonbassen.citindi.ui.BuildRepositories
import be.mariovonbassen.citindi.ui.MainViewModelFactory
import be.mariovonbassen.citindi.ui.components.AlertMessage
import be.mariovonbassen.citindi.ui.components.Footer
import be.mariovonbassen.citindi.ui.components.formatDate
import be.mariovonbassen.citindi.ui.provideMainDashBoardViewModel
import be.mariovonbassen.citindi.ui.states.ActiveStates.ActiveCityState
import be.mariovonbassen.citindi.ui.states.ActiveStates.ActiveUserState
import be.mariovonbassen.citindi.ui.states.AddCityState
import be.mariovonbassen.citindi.ui.theme.blueAppColor
import be.mariovonbassen.citindi.ui.theme.grayShade
import be.mariovonbassen.citindi.ui.viewmodels.MainDashBoardViewModel
import byteArrayToImage
import kotlinx.coroutines.flow.StateFlow
import java.util.Date


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainDashBoardScreen(navController: NavController) {

    val (userRepository, cityRepository) = BuildRepositories()

    val viewModelFactory = MainViewModelFactory(userRepository, cityRepository)
    val viewmodel = provideMainDashBoardViewModel(viewModelFactory)

    val active_user_state = viewmodel.globalActiveUserState
    val active_user_city_state = viewmodel.globalActiveCityState

    val state by viewmodel.state.collectAsState()
    val errorstate by viewmodel.errorState.collectAsState()
    val citySentences: List<CitySentence> by viewmodel.citySentenceList.observeAsState(emptyList())
    val citySightSeeings: List<CitySightSeeing> by viewmodel.citySightSeeingList.observeAsState(emptyList())

    Scaffold(

        bottomBar = {
            Footer(navController = navController)
        }

        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (errorstate.isError){
                    AlertMessage(alertText = errorstate.errorMessage)
                }

                CenterField(active_user_city_state = active_user_city_state)

                Spacer(modifier = Modifier.height(26.dp))

                ToDoDisplay(active_user_state = active_user_state)

                Spacer(modifier = Modifier.height(26.dp))

                Carousel(viewmodel, state, citySentences, citySightSeeings)

            }
        }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CenterField(active_user_city_state: StateFlow<ActiveCityState>) {

    val color = Color(android.graphics.Color.parseColor(blueAppColor))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .defaultMinSize(minHeight = 120.dp)
            .background(color = color),

    ) {

            active_user_city_state.value.activeCity?.let {

                ImageBitmapFromBytes(it.cityImage)

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                ) {

                Row(modifier = Modifier
                    .background(color=color),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        modifier = Modifier
                            .padding(10.dp, 3.dp, 3.dp, 3.dp),
                        text = it.cityName,
                        fontSize = 25.sp,
                        fontWeight = FontWeight(600),
                        color = Color.White
                    )

                    Text(
                        modifier = Modifier
                            .padding(0.dp, 3.dp, 10.dp, 3.dp),
                        text = it.country,
                        fontSize = 15.sp,
                        fontWeight = FontWeight(600),
                        color = Color.White
                    )

                }

                Spacer(modifier = Modifier.height(26.dp))

                DashboardData(arrivalDate = it.arrivalDate, leavingDate = it.leavingDate)
            }
        }
    }
}
@Composable
fun ImageBitmapFromBytes(byteArray: ByteArray){

    if (byteArray != null && byteArray.isNotEmpty() ){

        val imageBitmap = byteArrayToImage(byteArray)

        Image(
            modifier = Modifier
                .fillMaxSize(),
            bitmap = imageBitmap,
            contentDescription = "City Image",
            contentScale = ContentScale.FillBounds
        )
    }

}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun DashboardData(arrivalDate: Date, leavingDate: Date) {

        Row (
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 0.dp, 0.dp, 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "Arrival: ${formatDate(arrivalDate)}",
                fontSize = 18.sp, color = Color.White, fontWeight = FontWeight(600))

            Spacer(modifier = Modifier
                .height(15.dp))

            Text(text = "Leaving: ${formatDate(leavingDate)}",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight(600))
        }

}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ToDoDisplay(active_user_state: StateFlow<ActiveUserState>) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Column(

        ) {
            Row {
                Text(text = "Hey ${active_user_state.value.activeUser?.userName}", fontSize = 26.sp)

            }
            Row {
                Text(text = stringResource(id = R.string.plan_text), fontSize = 16.sp)
            }

        }
    }
}

@Composable
fun Carousel(viewmodel: MainDashBoardViewModel, state: AddCityState,
             citySentences: List<CitySentence>, citySightSeeings: List<CitySightSeeing>) {

    val grayShade = Color(android.graphics.Color.parseColor(grayShade))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = grayShade)
            .padding(10.dp, 0.dp, 10.dp, 0.dp)
    ) {
        
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                StackedCards(state, viewmodel, citySentences, citySightSeeings)
            }

        }
    }
}

@Composable
fun StackedCards(state: AddCityState, viewmodel: MainDashBoardViewModel,
                 citySentences: List<CitySentence>,
                 citySightSeeings: List<CitySightSeeing>) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(200.dp)
            .clickable(onClick = {
                viewmodel.onUserEvent(MainDashBoardEvent.showDialogCardClick(state.showDialog))
            }),

    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Box() {

                Column {

                Text(
                    text = stringResource(id = R.string.sentences_headline),
                    fontSize = 18.sp,
                    fontWeight = FontWeight(500)
                    )


                Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn {
                        items(citySentences) { item ->
                            Text(text = item.citySentence)
                            Spacer(
                                modifier = Modifier
                                    .height(5.dp)
                            )
                        }
                    }

                }

                if (state.showDialog) {
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        AddCitySentence(state, viewmodel, citySentences)
                    }
                }
            }

        }
    }

    Spacer(modifier = Modifier.height(10.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(200.dp)
            .clickable(onClick = {
                viewmodel.onUserEvent(
                    MainDashBoardEvent.showDialogSecondCardClick(state.showDialogSecondCard)
                )
            }),

        ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box() {

                Column{

                Text(
                    text = stringResource(id = R.string.sight_seeing_headline),
                    fontSize = 18.sp,
                    fontWeight = FontWeight(500)
                )

                Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn {
                        items(citySightSeeings) { item ->
                            Text(text = item.citySightSeeing)
                            Spacer(
                                modifier = Modifier
                                    .height(5.dp)
                            )
                        }
                    }

                }

                if (state.showDialogSecondCard) {
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        AddCitySightSeeing(state, viewmodel, citySightSeeings)
                    }
                }
            }
        }
    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AddCitySentence(state: AddCityState, viewmodel: MainDashBoardViewModel,
                    citySentences: List<CitySentence>){

        TextField(
        label = { },
        modifier = Modifier
            .width(250.dp)
            .height(50.dp),
        value = state.citySentence,
        onValueChange = {
            viewmodel.onUserEvent(
                MainDashBoardEvent.SetCitySentence(it)
            )
        })

        Button(onClick = {

                viewmodel.onUserEvent(MainDashBoardEvent.ConfirmCitySentence(citySentences))

        }) {
            Text(stringResource(id = R.string.add_text))
        }
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AddCitySightSeeing(state: AddCityState, viewmodel: MainDashBoardViewModel,
                       citySightSeeings: List<CitySightSeeing>){

    TextField(
        label = { },
        modifier = Modifier
            .width(250.dp)
            .height(50.dp),
        value = state.citySightSeeing,
        onValueChange = {
            viewmodel.onUserEvent(
                MainDashBoardEvent.SetCitySightSeeing(it)
            )
        })

    Button(onClick = {
        viewmodel.onUserEvent(MainDashBoardEvent.ConfirmCitySightSeeing(citySightSeeings))
    }) {
        Text(stringResource(id = R.string.add_text))
    }
}
