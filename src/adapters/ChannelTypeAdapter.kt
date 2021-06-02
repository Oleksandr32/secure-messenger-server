package adapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import models.Channel

class ChannelTypeAdapter : TypeAdapter<Channel>() {
    override fun write(out: JsonWriter?, value: Channel?) {
        when (value) {
            Channel.AUTH -> out?.value(AUTH_CHANNEL)
            Channel.USERS -> out?.value(USERS_CHANNEL)
            Channel.CHATS -> out?.value(CHATS_CHANNEL)
            Channel.CHAT -> out?.value(CHAT_CHANNEL)
            else -> out?.nullValue()
        }
    }

    override fun read(input: JsonReader?): Channel {
        return when (input?.nextString()) {
            AUTH_CHANNEL -> Channel.AUTH
            USERS_CHANNEL -> Channel.USERS
            CHATS_CHANNEL -> Channel.CHATS
            CHAT_CHANNEL -> Channel.CHAT
            else -> Channel.UNKNOWN
        }
    }

    companion object {
        private const val AUTH_CHANNEL = "auth"
        private const val USERS_CHANNEL = "users"
        private const val CHATS_CHANNEL = "chats"
        private const val CHAT_CHANNEL = "chat"
    }
}