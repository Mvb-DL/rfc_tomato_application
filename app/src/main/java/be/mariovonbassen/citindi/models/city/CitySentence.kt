package be.mariovonbassen.citindi.models.city

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "sentences", foreignKeys = [ForeignKey(
    entity = City::class,
    parentColumns = ["cityId"],
    childColumns = ["cityId"],
    onDelete = ForeignKey.CASCADE
)])
class CitySentence (
    val cityId: Int,
    val citySentence: String,
    @PrimaryKey(autoGenerate = true)
    val citySentenceId: Int = 0
)