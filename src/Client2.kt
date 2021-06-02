import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.runBlocking

object Client2 {
    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            val client = HttpClient(CIO).config { install(WebSockets) }

            client.ws(
                host = "0.0.0.0",
                port = 8080, path = "/ws"
            ) {
                // this: DefaultClientWebSocketSession

                // Send text frame.
                send("Hello, I'am client 2")

                // Send text frame.
                send(Frame.Text("Hello World 2"))

                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        println(frame.readText())
                    }
                }
            }
        }
    }
}