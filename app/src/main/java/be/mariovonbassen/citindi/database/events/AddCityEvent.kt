package be.mariovonbassen.citindi.database.events

import android.net.Uri
import java.net.URI
import java.util.Date

sealed interface AddCityEvent{
    data class SetCityName(val cityName: String): AddCityEvent
    data class SetArrivalDate(val arrivalDate: Date): AddCityEvent
    data class SetLeavingDate(val leavingDate: Date): AddCityEvent
    data class SetGpsPosition(val gpsPosition: String): AddCityEvent
    data class SetCountry(val country: String): AddCityEvent
    data class SetUriImage(val cityUri: Uri): AddCityEvent
    data class SetCityImage(val cityByteArray: ByteArray): AddCityEvent
    data class UpdateActiveCity(val cityId: Int): AddCityEvent
    object SetSurfaceOpacity: AddCityEvent
    object SetOpenDateField: AddCityEvent
    object ConfirmAddCity: AddCityEvent
}
