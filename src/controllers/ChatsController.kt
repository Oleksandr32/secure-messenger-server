package controllers

import com.google.gson.Gson
import io.ktor.http.cio.websocket.*
import models.Subscribe
import repositories.ChatsRepository

class ChatsController(
    private val gson: Gson,
    private val chatsRepository: ChatsRepository
) : ChannelsController {

    companion object {
        private const val GET_ACTION = "get"
    }

    override suspend fun subscribe(socket: WebSocketSession, subscribe: Subscribe) {
        when (subscribe.action) {
            GET_ACTION -> {
                val chats = chatsRepository.getUserChats(subscribe.userId)
                val response = gson.toJson(chats)
                socket.send(response)
            }
            else -> {

            }
        }
    }
}