package be.mariovonbassen.citindi.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mariovonbassen.citindi.database.events.MainDashBoardEvent
import be.mariovonbassen.citindi.database.repositories.CityRepository
import be.mariovonbassen.citindi.database.repositories.UserRepository
import be.mariovonbassen.citindi.models.city.CitySentence
import be.mariovonbassen.citindi.models.city.CitySightSeeing
import be.mariovonbassen.citindi.ui.components.ErrorType
import be.mariovonbassen.citindi.ui.states.ActiveStates.ActiveCityState
import be.mariovonbassen.citindi.ui.states.ActiveStates.ActiveUserState
import be.mariovonbassen.citindi.ui.states.ActiveStates.GlobalActiveCityState
import be.mariovonbassen.citindi.ui.states.ActiveStates.GlobalActiveUserState
import be.mariovonbassen.citindi.ui.states.AddCityErrorState
import be.mariovonbassen.citindi.ui.states.AddCityState
import be.mariovonbassen.citindi.ui.states.LoginErrorState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainDashBoardViewModel(
    private val cityRepository: CityRepository,
) : ViewModel() {

    val globalActiveUserState: StateFlow<ActiveUserState> = GlobalActiveUserState.activeState
    val globalActiveCityState: StateFlow<ActiveCityState> = GlobalActiveCityState.activeState

    private val _state = MutableStateFlow(AddCityState())
    val state: StateFlow<AddCityState> = _state.asStateFlow()

    private val _errorState = MutableStateFlow(AddCityErrorState())
    val errorState: StateFlow<AddCityErrorState> = _errorState.asStateFlow()

    private var _citySentenceList = MutableLiveData<List<CitySentence>>(state.value.citySentenceList)
    var citySentenceList: LiveData<List<CitySentence>> = _citySentenceList

    private var _citySightSeeingList = MutableLiveData<List<CitySightSeeing>>(state.value.citySightSeeingList)
    var citySightSeeingList: LiveData<List<CitySightSeeing>> = _citySightSeeingList

    val cityId = globalActiveCityState.value.activeCity?.cityId

    init {

        if (cityId != null) {
            loadData(cityId)
        }
    }

    private fun loadData(cityId: Int) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val citySentences = cityRepository.getCitySentencesForCity(cityId)
                _citySentenceList.postValue(citySentences)

                val citySightSeeings = cityRepository.getCitySightSeeingForCity(cityId)
                _citySightSeeingList.postValue(citySightSeeings)

            }
        }
    }

    fun onUserEvent(event: MainDashBoardEvent) {

        when (event) {

            is MainDashBoardEvent.SetCitySentence -> {

                _state.update {
                    it.copy(
                        citySentence = event.citySentence
                    )
                }
            }

            is MainDashBoardEvent.SetCitySightSeeing -> {

                _state.update {
                    it.copy(
                        citySightSeeing = event.citySightSeeing
                    )
                }
            }

            is MainDashBoardEvent.showDialogCardClick -> {

                _state.update {
                    it.copy(
                        showDialog = !event.showDialog
                    )
                }
            }

            is MainDashBoardEvent.showDialogSecondCardClick -> {

                _state.update {
                    it.copy(
                        showDialogSecondCard = !event.showDialogSecondCard
                    )
                }
            }

            is MainDashBoardEvent.ConfirmCitySentence -> {

                val validatedInput = validateCardInputs()

                if (validatedInput) {

                    suspend fun addCitySentence(cityId: Int, sentence: String) {
                        val newCitySentence = CitySentence(cityId, sentence)
                        cityRepository.insertCitySentence(newCitySentence)
                        val citySentences = cityRepository.getCitySentencesForCity(cityId)
                        _citySentenceList.value = citySentences
                    }

                    viewModelScope.launch {

                        if (cityId != null) {
                            addCitySentence(cityId, state.value.citySentence)
                        }

                        _state.update {
                            it.copy(
                                showDialog = false
                            )
                        }

                        _state.update {
                            it.copy(
                                citySentence = ""
                            )
                        }
                    }
                }

            }

            is MainDashBoardEvent.ConfirmCitySightSeeing -> {

                val validatedInput = validateSightSeeingCardInputs()

                if (validatedInput) {

                    suspend fun addCitySightSeeing(cityId: Int, sightSeeing: String) {
                        val newCitySightSeeing = CitySightSeeing(cityId, sightSeeing)
                        cityRepository.insertCitySightSeeing(newCitySightSeeing)
                        val citySightSeeing = cityRepository.getCitySightSeeingForCity(cityId)
                        _citySightSeeingList.value = citySightSeeing
                    }

                    viewModelScope.launch {

                        if (cityId != null) {
                            addCitySightSeeing(cityId, state.value.citySightSeeing)
                        }

                        _state.update {
                            it.copy(
                                showDialogSecondCard = false
                            )
                        }

                        _state.update {
                            it.copy(
                                citySightSeeing = ""
                            )
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

    private fun validateCardInputs(): Boolean {

        val citySentence = state.value.citySentence.trim()

        return when {

            citySentence.isEmpty() -> {

                if (errorState.value.errorType == ErrorType.ADDCITY_ERROR) {

                    _errorState.update {
                        it.copy(
                            isError = true,
                            errorMessage = "Sentence is empty!"
                        )
                    }
                }

                false
            }

            cityId == null -> {

                if (errorState.value.errorType == ErrorType.ADDCITY_ERROR) {

                    _errorState.update {
                        it.copy(
                            isError = true,
                            errorMessage = "Please add a city first of all!"
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

    private fun validateSightSeeingCardInputs(): Boolean {

        val citySightSeeing = state.value.citySightSeeing.trim()

        return when {

            citySightSeeing.isEmpty() -> {

                if (errorState.value.errorType == ErrorType.ADDCITY_ERROR) {

                    _errorState.update {
                        it.copy(
                            isError = true,
                            errorMessage = "Sightseeing is empty!"
                        )
                    }
                }

                false
            }

            cityId == null -> {

                if (errorState.value.errorType == ErrorType.ADDCITY_ERROR) {

                    _errorState.update {
                        it.copy(
                            isError = true,
                            errorMessage = "Please add a city first of all!"
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