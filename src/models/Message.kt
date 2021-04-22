package models

data class Message(
    val id: Int? = null,
    val chatId: Int,
    val body: String,
    val date: String,
    var isMine: Boolean
)