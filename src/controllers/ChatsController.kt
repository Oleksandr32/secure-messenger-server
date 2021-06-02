package controllers

import models.Subscribe
import repositories.ChatsRepository
import server.SecureMessengerSession
import server.ServerResultsListener

class ChatsController(private val chatsRepository: ChatsRepository) : Subscribable {

    companion object {
        private const val GET_ACTION = "get"
        private const val CREATE_ACTION = "create"
    }

    override suspend fun subscribe(
        listener: ServerResultsListener,
        session: SecureMessengerSession,
        subscribe: Subscribe
    ) {
        when (subscribe.action) {
            GET_ACTION -> {
                val chats = chatsRepository.getUserChats(subscribe.userId)
                listener.onResult(session, chats)
            }
            CREATE_ACTION -> {
                val otherUserId = subscribe.data!!.value as Int
                val newChat = chatsRepository.createChat(subscribe.userId, otherUserId)
                listener.onResult(session, newChat)
                listener.onResult(otherUserId, newChat.apply { swapUsers() })
            }
            else -> {
            }
        }
    }
}