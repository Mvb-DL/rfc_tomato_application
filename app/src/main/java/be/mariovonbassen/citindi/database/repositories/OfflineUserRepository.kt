package be.mariovonbassen.citindi.database.repositories

import be.mariovonbassen.citindi.database.dao.CityDao
import be.mariovonbassen.citindi.database.dao.UserDao
import be.mariovonbassen.citindi.models.User
import be.mariovonbassen.citindi.models.city.City
import kotlinx.coroutines.flow.Flow

class OfflineUserRepository(private val userDao: UserDao) : UserRepository {

    override fun getAllUsers(): Flow<List<User>> { return userDao.getAllUsers() }

    override fun getUser(userId: Int): User = userDao.getUser(userId)

    override suspend fun upsertUser(user: User) = userDao.upsertUser(user)

    override suspend fun deleteUser(user: User) = userDao.deleteUser(user)

    override suspend fun checkUserPresence(password: String, userName: String): Boolean {
        val user = userDao.getUserByPasswordAndUserName(password, userName)
        return user != null
    }

    override suspend fun getUserByPasswordAndUserName(password: String, userName: String): User {
        val user = userDao.getUserByPasswordAndUserName(password, userName)
        return user
    }

}
