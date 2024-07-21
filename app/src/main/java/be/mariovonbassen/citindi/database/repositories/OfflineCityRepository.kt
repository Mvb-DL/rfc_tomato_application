package be.mariovonbassen.citindi.database.repositories

import be.mariovonbassen.citindi.database.dao.CityDao
import be.mariovonbassen.citindi.models.city.City
import be.mariovonbassen.citindi.models.city.CitySentence
import be.mariovonbassen.citindi.models.city.CitySightSeeing

class OfflineCityRepository(private val cityDao: CityDao) : CityRepository  {

    override suspend fun upsertCity(city: City) = cityDao.upsertCity(city)

    override suspend fun deleteCity(city: City) = cityDao.deleteCity(city)

    override suspend fun getCitiesByUserId(userId: Int): List<City>
        { return cityDao.getCitiesByUserId(userId) }

    override suspend fun getCityByCityId(cityId: Int): City {
        return cityDao.getCityByCityId(cityId)
    }
    override suspend fun getLatestCity(userId: Int): City {
        return cityDao.getLatestCity(userId)
    }
    override suspend fun insertCitySentence(citySentence: CitySentence) {
        cityDao.insertCitySentence(citySentence)
    }
    override suspend fun getCitySentencesForCity(cityId: Int): List<CitySentence> {
        return cityDao.getCitySentencesForCity(cityId)
    }
    override suspend fun insertCitySightSeeing(citySightSeeing: CitySightSeeing) {
        cityDao.insertCitySightSeeing(citySightSeeing)
    }
    override suspend fun getCitySightSeeingForCity(cityId: Int): List<CitySightSeeing> {
        return cityDao.getCitySightSeeingForCity(cityId)
    }

}