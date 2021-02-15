import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.sessions.*
import kotlinx.coroutines.channels.ClosedSendChannelException

inline fun <reified T> ApplicationCall.setSessionIfNotExist(session: T) {
    if (sessions.get<T>() == null) {
        sessions.set(session)
    }
}

suspend fun List<WebSocketSession>.send(frame: Frame) {
    forEach {
        try {
            it.send(frame.copy())
        } catch (error: Throwable) {
            try {
                it.close(CloseReason(CloseReason.Codes.PROTOCOL_ERROR, ""))
            } catch (ignore: ClosedSendChannelException) {
            }
        }
    }
}