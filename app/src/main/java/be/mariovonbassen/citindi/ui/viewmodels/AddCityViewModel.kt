package be.mariovonbassen.citindi.ui.viewmodels


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mariovonbassen.citindi.database.events.AddCityEvent
import be.mariovonbassen.citindi.database.repositories.CityRepository
import be.mariovonbassen.citindi.database.repositories.UserRepository
import be.mariovonbassen.citindi.models.city.City
import be.mariovonbassen.citindi.models.city.CitySentence
import be.mariovonbassen.citindi.ui.components.ErrorType
import be.mariovonbassen.citindi.ui.states.ActiveStates.ActiveCityState
import be.mariovonbassen.citindi.ui.states.ActiveStates.ActiveUserState
import be.mariovonbassen.citindi.ui.states.ActiveStates.GlobalActiveCityState
import be.mariovonbassen.citindi.ui.states.ActiveStates.GlobalActiveUserState
import be.mariovonbassen.citindi.ui.states.AddCityErrorState
import be.mariovonbassen.citindi.ui.states.AddCityState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Date


class AddCityViewModel(
    private val cityRepository: CityRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AddCityState())
    val state: StateFlow<AddCityState> = _state.asStateFlow()

    private var _userCityList = MutableLiveData<List<City>>(state.value.userCities)
    var userCityList: LiveData<List<City>> = _userCityList

    private val _errorState = MutableStateFlow(AddCityErrorState())
    val errorState: StateFlow<AddCityErrorState> = _errorState.asStateFlow()

    private val globalActiveUserState: StateFlow<ActiveUserState> = GlobalActiveUserState.activeState
    val userId = globalActiveUserState.value.activeUser?.userId

    init {

        if (userId != null) {
            loadData(userId)
        }
    }

    private fun loadData(userId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val userCities = cityRepository.getCitiesByUserId(userId)
                _userCityList.postValue(userCities)

            }
        }
    }

    fun onUserEvent(event: AddCityEvent) {

        when (event) {

            is AddCityEvent.SetCityName-> {
                _state.update {
                    it.copy(cityName = event.cityName)
                }
            }

            is AddCityEvent.SetArrivalDate-> {
                _state.update {
                    it.copy(arrivalDate = event.arrivalDate)
                }
            }

            is AddCityEvent.SetLeavingDate-> {
                _state.update {
                    it.copy(leavingDate = event.leavingDate)
                }
            }

            is AddCityEvent.SetGpsPosition-> {
                _state.update {
                    it.copy(gpsPosition = event.gpsPosition)
                }
            }

            is AddCityEvent.SetCountry-> {
                _state.update {
                    it.copy(country = event.country)
                }
            }

            is AddCityEvent.SetUriImage-> {

                _state.update {
                    it.copy(imageURI = event.cityUri)
                }

            }

            is AddCityEvent.SetCityImage-> {

                _state.update {
                    it.copy(cityImage = event.cityByteArray)
                }

            }

            is AddCityEvent.SetSurfaceOpacity-> {

                if (state.value.surfaceOpacity == 1.0f) {
                    _state.update {
                        it.copy(surfaceOpacity = 0.4f)
                    }
                }else{
                    _state.update {
                        it.copy(surfaceOpacity = 1.0f)
                    }
                }

            }

            is AddCityEvent.SetOpenDateField-> {

                if(state.value.openDateField){
                    _state.update {
                        it.copy(openDateField = false)
                    }
                }else{
                    _state.update {
                        it.copy(openDateField = true)
                    }
                }
            }

            is AddCityEvent.UpdateActiveCity-> {

                viewModelScope.launch {

                    withContext(Dispatchers.IO) {

                        val activeCity = cityRepository.getCityByCityId(event.cityId)

                        val updatedCityState =
                            ActiveCityState(activeCity = activeCity, isActive = true)

                        GlobalActiveCityState.updateCityAppState(updatedCityState)

                        _state.update {
                            it.copy(updatedActiveCity = true)
                        }

                    }
                }
            }

            is AddCityEvent.ConfirmAddCity-> {

                val cityName = state.value.cityName
                val arrivalDate = state.value.arrivalDate
                val leavingDate = state.value.leavingDate
                val gpsPosition = state.value.gpsPosition
                val country = state.value.country
                val cityImage = state.value.cityImage
                val userId = globalActiveUserState.value.activeUser?.userId

                val validatedAddCityInput = validateAddCityInputs()

                if (validatedAddCityInput && userId != null) {

                    suspend fun addCity(
                        cityName: String,
                        arrivalDate: Date, leavingDate: Date,
                        gpsPosition: String, country: String,
                        cityImage: ByteArray, userId: Int
                    ) {
                        val city = City(
                            userId, cityName, arrivalDate,
                            leavingDate, gpsPosition, country, cityImage
                        )
                        cityRepository.upsertCity(city)
                        val userCities = cityRepository.getCitiesByUserId(userId)
                        _userCityList.postValue(userCities)
                    }

                    viewModelScope.launch {

                        withContext(Dispatchers.IO) {

                            addCity(
                                cityName,
                                arrivalDate,
                                leavingDate,
                                gpsPosition,
                                country,
                                cityImage,
                                userId
                            )

                            val latestCity = cityRepository.getLatestCity(userId)

                            val updatedCityState =
                                ActiveCityState(activeCity = latestCity, isActive = true)

                            GlobalActiveCityState.updateCityAppState(updatedCityState)

                            _state.update {
                                it.copy(isAddingSuccessful = true)
                            }
                        }
                    }
                }
            }
        }
    }

    fun resetError(){
        _errorState.update {
            it.copy(isError = false)
        }
    }

    private fun validateAddCityInputs(): Boolean {

        val cityName = state.value.cityName.trim()
        val country = state.value.country.trim()

        return when {

            cityName.isEmpty() -> {

                if (errorState.value.errorType == ErrorType.ADDCITY_ERROR) {

                    _errorState.update {
                        it.copy(
                            isError = true,
                            errorMessage = "Cityname can´t be empty!"
                        )
                    }
                }

                false
            }

            country.isEmpty() -> {

                if (errorState.value.errorType == ErrorType.ADDCITY_ERROR) {

                    _errorState.update {
                        it.copy(
                            isError = true,
                            errorMessage = "Country can´t be empty!"
                        )
                    }
                }

                false
            }


            else -> {

                _errorState.update {
                    it.copy(
                        isError = false,
                        errorMessage = ""
                    )
                }

                true
            }
        }
    }
}