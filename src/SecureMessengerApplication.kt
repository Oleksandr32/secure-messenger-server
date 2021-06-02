import database.Database
import di.appModule
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.*
import org.koin.ktor.ext.*
import org.koin.ktor.ext.inject
import java.time.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    SecureMessengerApplication().apply {
        Database.init()
        install(DefaultHeaders)
        install(CallLogging)
        install(WebSockets) {
            pingPeriod = Duration.ofMinutes(1)
        }
        install(Sessions) {
            cookie<SecureMessengerSession>("SESSION")
        }
        install(Koin) {
            modules(appModule)
        }

        main()
    }
}

data class SecureMessengerSession(val id: String)

class SecureMessengerApplication {

    fun Application.main() {
        val server by inject<SecureMessengerServer>()

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

                server.memberJoin(session, this)

                try {
                    incoming.consumeEach {
                        server.onMessageReceived(session, it)
                    }
                } finally {
                    server.memberLeft(session, this)
                }
            }
        }
    }
}