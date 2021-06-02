package repositories

import database.TempStorage
import database.mappers.ChatModelMapper
import database.mappers.MessageModelMapper
import database.models.MessageModel
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.channels.BroadcastChannel
import models.*
import org.joda.time.DateTime

interface ChatsRepository {

    fun getUserChats(userId: Int): List<Chat>

    fun getChat(userId: Int, id: Int): Chat

    suspend fun createChat(firstUserId: Int, secondUserId: Int): Chat

    fun addMessage(userId: Int, chatId: Int, body: String): Message

    fun getMessages(chatId: Int, userId: Int): List<Message>
}

class ChatsRepositoryImpl(private val tempStorage: TempStorage) : ChatsRepository {

    private val messageModelMapper = MessageModelMapper()
    private val chatModelMapper = ChatModelMapper()
    private var chatCounter = 0
    private var messageCounter = 0

    override fun getUserChats(userId: Int): List<Chat> {

        return tempStorage.chats
            .filter { model -> model.firstUser.id == userId || model.secondUser.id == userId }
            .map { model -> chatModelMapper.toChat(userId, model) }
    }

    override fun getChat(userId: Int, id: Int): Chat {
        val model =  tempStorage.chats.first { model -> model.id == id }
        return chatModelMapper.toChat(userId, model)
    }

    override suspend fun createChat(firstUserId: Int, secondUserId: Int): Chat {
        val firstUser = tempStorage.users.find { it.id == firstUserId }!!
        val secondUser = tempStorage.users.find { it.id == secondUserId }!!
        val newChat = Chat(chatCounter++, firstUser, secondUser, ChatStatus.ACTIVE)
        tempStorage.chats.add(chatModelMapper.fromChat(newChat))
        return newChat
    }

    override fun addMessage(userId: Int, chatId: Int, body: String): Message {
        val model = MessageModel(
            id = messageCounter++,
            chatId = chatId,
            body = body,
            date = DateTime.now().toString(),
            userId = userId
        )
        tempStorage.messages.add(model)
        return messageModelMapper.toMessage(userId, model)
    }

    override fun getMessages(chatId: Int, userId: Int): List<Message> {
        return tempStorage.messages
            .filter { it.chatId == chatId }
            .map { model -> messageModelMapper.toMessage(userId, model) }
    }
}