package be.mariovonbassen.citindi.models.city

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "city_sightseeing", foreignKeys = [ForeignKey(
    entity = City::class,
    parentColumns = ["cityId"],
    childColumns = ["cityId"],
    onDelete = ForeignKey.CASCADE // Cascade delete when City is deleted
)])
data class CitySightSeeing(
    val cityId: Int,
    val citySightSeeing: String,
    @PrimaryKey(autoGenerate = true)
    val citySightSeeingId: Int = 0
)
