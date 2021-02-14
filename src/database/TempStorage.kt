package database

import models.*

class TempStorage {
    val users = arrayListOf(
        User(0, "Alex", "Lysun", "alex_lysun"),
        User(1, "Sergii", "Lysun", "sergii_lysun")
    )
    val chats = arrayListOf(
        Chat(0, "Alex_Sergii", Pair(users[0], users[1]), arrayListOf())
    )

    fun init() {
    }
}