package adapters

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken.*
import com.google.gson.stream.JsonWriter
import models.Data
import java.io.IOException

class DataTypeAdapter : TypeAdapter<Data>() {
    private val delegate = Gson().getAdapter(Any::class.java)

    @Throws(IOException::class)
    override fun write(out: JsonWriter?, data: Data) {
        delegate.write(out, data.value)
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): Data? {
        val value = if (reader.peek() == NUMBER) {
            val num = reader.nextString()
            if (num.contains('.')) num.toDouble() else num.toInt()
        } else {
            delegate.read(reader)
        }
        return Data(value)
    }
}