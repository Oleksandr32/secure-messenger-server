package adapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import models.Channel

class ChannelTypeAdapter : TypeAdapter<Channel>() {
    override fun write(out: JsonWriter?, value: Channel?) {
        when (value) {
            Channel.CHATS -> out?.value(CHATS_CHANNEL)
            else -> out?.nullValue()
        }
    }

    override fun read(input: JsonReader?): Channel {
        return when (input?.nextString()) {
            CHATS_CHANNEL -> Channel.CHATS
            else -> Channel.UNKNOWN
        }
    }

    companion object {
        private const val CHATS_CHANNEL = "chats"
    }
}