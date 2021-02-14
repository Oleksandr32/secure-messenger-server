package models

data class Message(
    val id: Int,
    val body: String,
    val date: String,
    val userId: Int
)