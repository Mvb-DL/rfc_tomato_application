package be.mariovonbassen.citindi

import android.app.Application
import be.mariovonbassen.citindi.database.AppContainer
import be.mariovonbassen.citindi.database.AppDataContainer
import be.mariovonbassen.citindi.database.repositories.UserRepository


class CitIndiApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
