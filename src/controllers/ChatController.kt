package controllers

import models.*
import repositories.ChatsRepository
import server.SecureMessengerSession
import server.ServerResultsListener

class ChatController(private val chatsRepository: ChatsRepository) : Subscribable {

    companion object {
        private const val GET_ACTION = "get"
        private const val GET_MESSAGES_ACTION = "get-messages"
        private const val SEND_MESSAGE_ACTION = "send-message"
    }

    override suspend fun subscribe(
        listener: ServerResultsListener,
        session: SecureMessengerSession,
        subscribe: Subscribe
    ) {
        when (subscribe.action) {
            GET_ACTION -> {
                val chatId = subscribe.data!!.value as Int
                val chat = chatsRepository.getChat(subscribe.userId, chatId)
                listener.onResult(session, chat)
            }
            GET_MESSAGES_ACTION -> {
                val chatId = subscribe.data!!.value as Int
                val messages = chatsRepository.getMessages(chatId, subscribe.userId)
                listener.onResult(session, messages)
            }
            SEND_MESSAGE_ACTION -> {
                val request = subscribe.data!!.value as Map<String, String>
                val chatId = request["chatId"]!!.toInt()
                val body = request["body"]!!
                val message = chatsRepository.addMessage(subscribe.userId, chatId, body)
                listener.onResult(session, message)
                val otherUser = chatsRepository.getChat(subscribe.userId, chatId).other
                listener.onResult(otherUser.id!!, message.apply { isMine = false })
            }
            else -> {
            }
        }
    }
}