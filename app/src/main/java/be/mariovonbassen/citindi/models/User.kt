package be.mariovonbassen.citindi.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    var userName: String,
    var password: String,
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0
)