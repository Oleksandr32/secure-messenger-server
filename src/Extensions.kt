import io.ktor.application.*
import io.ktor.sessions.*
import io.ktor.util.*

inline fun <reified T> ApplicationCall.setSessionIfNotExist(session: T) {
    if (sessions.get<T>() == null) {
        sessions.set(session)
    }
}