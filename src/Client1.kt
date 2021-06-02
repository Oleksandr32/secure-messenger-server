import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.runBlocking
import models.Chat

object Client1 {
    private val gson = Gson()

    data class Subscribe(
        val userId: Int,
        val channel: String,
        val action: String,
        val data: Any? = null
    )

    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            val client = HttpClient(CIO).config { install(WebSockets) }

            client.ws(
                host = "192.168.6.106",
                port = 8080, path = "/ws"
            ) {
                // this: DefaultClientWebSocketSession

                val subscribe = Subscribe(0, "chats", "get")
                send(gson.toJson(subscribe))

                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                       // val chat = gson.fromJson(frame.readText(), Chat::class.java)
                        println(frame.readText())
                    }
                }
            }
        }
    }
}