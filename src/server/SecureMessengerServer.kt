package server

import com.google.gson.Gson
import controllers.*
import io.ktor.http.cio.websocket.*
import models.*
import java.util.concurrent.*

class SecureMessengerServer(
    private val gson: Gson,
    private val authController: AuthController,
    private val usersController: UsersController,
    private val chatsController: ChatsController,
    private val chatController: ChatController
) : ServerResultsListener {

    private val members = ConcurrentHashMap<SecureMessengerSession, WebSocketSession>()

    fun memberJoin(session: SecureMessengerSession, socket: WebSocketSession) {
        members.computeIfPresent(session) { _, _ -> socket }
        members.computeIfAbsent(session) { socket }
    }

    fun memberLeft(session: SecureMessengerSession) {
        val key = members.keys.firstOrNull { it.id == session.id }
        members.remove(key)
    }

    suspend fun onMessageReceived(session: SecureMessengerSession, frame: Frame) {
        if (frame is Frame.Text) {
            val subscribe = gson.fromJson(frame.readText(), Subscribe::class.java)
            subscribe(session, subscribe)
        }
    }

    override suspend fun onLogin(session: SecureMessengerSession, user: User) {
        val socket = getSocket(session)!!
        members.remove(session)
        val newSession = session.apply { this.user = user }
        members[newSession] = socket
    }

    override suspend fun onResult(session: SecureMessengerSession, result: Any) {
        val socket = getSocket(session)
        val response = gson.toJson(result)
        socket?.send(response)
    }

    override suspend fun onResult(userId: Int, result: Any) {
        val session = members.keys.firstOrNull { it.user?.id == userId }
        val socket = getSocket(session)
        val response = gson.toJson(result)
        socket?.send(response)
    }

    private fun getSocket(session: SecureMessengerSession?): WebSocketSession? {
        val key = members.keys.firstOrNull { it.id == session?.id }
        return if (key != null) members[key] else null
    }

    private suspend fun subscribe(session: SecureMessengerSession, subscribe: Subscribe) {
        when (subscribe.channel) {
            Channel.AUTH -> authController.subscribe(this, session, subscribe)
            Channel.USERS -> usersController.subscribe(this, session, subscribe)
            Channel.CHATS -> chatsController.subscribe(this, session, subscribe)
            Channel.CHAT -> chatController.subscribe(this, session, subscribe)
            Channel.UNKNOWN -> {
            }
        }
    }
}