package controllers

import io.ktor.http.cio.websocket.*
import models.Channel.*
import models.Subscribe

interface ChannelsController {

    suspend fun subscribe(socket: WebSocketSession, subscribe: Subscribe)
}

class ChannelsControllerImpl(
    private val chatsController: ChatsController
) : ChannelsController {

    override suspend fun subscribe(socket: WebSocketSession, subscribe: Subscribe) {
        when (subscribe.channel) {
            CHATS -> chatsController.subscribe(socket, subscribe)
            UNKNOWN -> {
            }
        }
    }
}