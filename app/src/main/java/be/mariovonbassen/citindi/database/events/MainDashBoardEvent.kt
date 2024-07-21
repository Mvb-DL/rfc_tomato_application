package be.mariovonbassen.citindi.database.events

import be.mariovonbassen.citindi.models.city.CitySentence
import be.mariovonbassen.citindi.models.city.CitySightSeeing

sealed interface MainDashBoardEvent{
    data class SetCitySentence(val citySentence: String): MainDashBoardEvent
    data class ConfirmCitySentence(val citySentenceList: List<CitySentence>) : MainDashBoardEvent
    data class SetCitySightSeeing(val citySightSeeing: String): MainDashBoardEvent
    data class ConfirmCitySightSeeing(val citySightSeeing: List<CitySightSeeing>) : MainDashBoardEvent
    data class showDialogCardClick(val showDialog: Boolean) : MainDashBoardEvent
    data class showDialogSecondCardClick(val showDialogSecondCard: Boolean) : MainDashBoardEvent
}