package repositories

import database.TempStorage
import models.Chat

interface ChatsRepository {

    fun getUserChats(userId: Int): List<Chat>
}

class ChatsRepositoryImpl(private val tempStorage: TempStorage) : ChatsRepository {

    override fun getUserChats(userId: Int): List<Chat> {
        return tempStorage.chats.filter { chat -> chat.users.toList().any { it.id == userId } }
    }
}