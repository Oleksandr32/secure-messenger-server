package database.mappers

import database.models.ChatModel
import models.Chat


class ChatModelMapper {

    fun toChat(userId: Int, model: ChatModel): Chat {
        val isMeFirst = model.firstUser.id == userId
        return Chat(
            id = model.id,
            me = if (isMeFirst) model.firstUser else model.secondUser,
            other = if (!isMeFirst) model.firstUser else model.secondUser,
            status = model.status,
            messages = model.messages
        )
    }

    fun fromChat(chat: Chat): ChatModel {
        return ChatModel(
            id = chat.id,
            firstUser = chat.me,
            secondUser = chat.other,
            status = chat.status,
            messages = chat.messages
        )
    }
}