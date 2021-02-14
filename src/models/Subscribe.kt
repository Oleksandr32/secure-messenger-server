package models

data class Subscribe(
    val userId: Int,
    val channel: Channel,
    val action: String,
    val data: Any?
)