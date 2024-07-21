package be.mariovonbassen.citindi.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import be.mariovonbassen.citindi.models.city.City
import be.mariovonbassen.citindi.models.city.CitySentence
import be.mariovonbassen.citindi.models.city.CitySightSeeing


@Dao
interface CityDao {
    @Upsert
    suspend fun upsertCity(city: City)
    @Delete
    suspend fun deleteCity(city: City)
    @Query("SELECT * FROM cities WHERE userId = :userId")
    fun getCitiesByUserId(userId: Int): List<City>
    @Query("SELECT * FROM cities WHERE cityId = :cityId")
    fun getCityByCityId(cityId: Int): City
    @Query("SELECT * FROM cities WHERE userId = :userId ORDER BY cityId DESC LIMIT 1")
    suspend fun getLatestCity(userId: Int): City
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCitySentence(citySentence: CitySentence)
    @Query("SELECT * FROM sentences WHERE cityId = :cityId")
    suspend fun getCitySentencesForCity(cityId: Int): List<CitySentence>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCitySightSeeing(citySightSeeing: CitySightSeeing)
    @Query("SELECT * FROM city_sightseeing WHERE cityId = :cityId")
    suspend fun getCitySightSeeingForCity(cityId: Int): List<CitySightSeeing>

}