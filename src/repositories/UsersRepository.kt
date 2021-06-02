package repositories

import database.TempStorage
import models.User

interface UsersRepository {

    fun getUser(id: Int): User?

    fun getUser(userName: String): User?

    fun getUsers(query: String): List<User>

    fun saveUser(user: User)

    fun updateUser(user: User)
}

class UsersRepositoryImpl(private val tempStorage: TempStorage) : UsersRepository {

    private var userCounter = 0

    override fun getUser(id: Int): User? {
        return tempStorage.users.firstOrNull { it.id == id }
    }

    override fun getUser(userName: String): User? {
        return tempStorage.users.firstOrNull { it.userName == userName }
    }

    override fun getUsers(query: String): List<User> {
        return tempStorage.users.filter { user -> user.userName!!.contains(query) }
    }

    override fun saveUser(user: User) {
        tempStorage.users.add(user.apply { id = userCounter++ })
    }

    override fun updateUser(user: User) {
        val updateIndex = tempStorage.users.indexOfFirst { it.id == user.id }
        tempStorage.users[updateIndex] = user
    }
}