package database

import database.models.ChatModel
import database.models.MessageModel
import models.*

class TempStorage {

    val users = arrayListOf<User>()

    val chats = arrayListOf<ChatModel>()

    val messages = arrayListOf<MessageModel>()

    fun init() {
    }
}