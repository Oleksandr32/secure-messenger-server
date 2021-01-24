import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.*
import java.time.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    SecureMessengerApplication().apply {
        install(DefaultHeaders)
        install(CallLogging)
        install(WebSockets) {
            pingPeriod = Duration.ofMinutes(1)
        }
        install(Sessions) {
            cookie<SecureMessengerSession>("SESSION")
        }

        main()
    }
}

data class SecureMessengerSession(val id: String)

class SecureMessengerApplication {
    private val server = SecureMessengerServer()

    fun Application.main() {
        intercept(ApplicationCallPipeline.Features) {
            call.setSessionIfNotExist(SecureMessengerSession(generateNonce()))
        }

        routing {
            webSocket("/ws") {
                val session = call.sessions.get<SecureMessengerSession>()
                if (session == null) {
                    close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session"))
                    return@webSocket
                }

                server.memberJoin(session.id, this)

                try {
                    incoming.consumeEach { frame ->
                        if (frame is Frame.Text) {
                            receivedMessage(session.id, frame.readText())
                        }
                    }
                } finally {
                    server.memberLeft(session.id, this)
                }
            }
        }
    }

    private suspend fun receivedMessage(id: String, message: String) {
        server.message(id, message)
        println("receivedMessage: $message")
    }
}