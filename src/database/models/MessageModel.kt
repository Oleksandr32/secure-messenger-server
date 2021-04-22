package database.models

data class MessageModel(
    val id: Int,
    val chatId: Int,
    val body: String,
    val date: String,
    val userId: Int
)