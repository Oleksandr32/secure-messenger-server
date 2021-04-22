package database.models

import models.ChatStatus
import models.Message
import models.User

data class ChatModel(
    val id: Int,
    val firstUser: User,
    val secondUser: User,
    val status: ChatStatus,
    val messages: ArrayList<Message> = arrayListOf()
)