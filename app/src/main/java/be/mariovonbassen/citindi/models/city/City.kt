package be.mariovonbassen.citindi.models.city

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import be.mariovonbassen.citindi.models.User
import java.util.Date


@Entity(tableName = "cities", foreignKeys = [ForeignKey(
    entity = User::class,
    parentColumns = ["userId"],
    childColumns = ["userId"],
    onDelete = ForeignKey.CASCADE
)])
data class City (
    val userId: Int,
    val cityName: String,
    val arrivalDate: Date,
    val leavingDate: Date,
    val gpsPosition: String,
    val country: String,
    val cityImage: ByteArray,
    @PrimaryKey(autoGenerate = true)
    val cityId: Int = 0,
)