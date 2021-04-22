package models

data class Subscribe(
    var userId: Int = 0,
    val channel: Channel,
    val action: String,
    val data: Data?
)