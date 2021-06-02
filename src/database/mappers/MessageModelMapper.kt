package database.mappers

import database.models.MessageModel
import models.Message


class MessageModelMapper {

    fun toMessage(userId: Int, model: MessageModel): Message {
        return Message(
            id = model.id,
            chatId = model.chatId,
            body = model.body,
            date = model.date,
            isMine = model.userId == userId
        )
    }
}