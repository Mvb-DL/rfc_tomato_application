package be.mariovonbassen.citindi.database.repositories

import be.mariovonbassen.citindi.models.User
import be.mariovonbassen.citindi.models.city.City
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getAllUsers(): Flow<List<User>>

    fun getUser(userId: Int): User

    suspend fun upsertUser(user: User)

    suspend fun deleteUser(user: User)

    suspend fun checkUserPresence(password: String, userName: String): Boolean

    suspend fun getUserByPasswordAndUserName(password: String, userName: String): User



}