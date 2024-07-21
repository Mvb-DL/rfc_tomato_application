package be.mariovonbassen.citindi.database

import android.content.Context
import be.mariovonbassen.citindi.database.repositories.CityRepository
import be.mariovonbassen.citindi.database.repositories.OfflineCityRepository
import be.mariovonbassen.citindi.database.repositories.OfflineUserRepository
import be.mariovonbassen.citindi.database.repositories.UserRepository

interface AppContainer {
    val userRepository: UserRepository
    val cityRepository: CityRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val userRepository: UserRepository by lazy {
        OfflineUserRepository(UserDatabase.getDatabase(context).userDao())
    }

    override val cityRepository: CityRepository by lazy {
        OfflineCityRepository(
            UserDatabase.getDatabase(context).cityDao()
        )
    }

}