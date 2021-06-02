import com.google.gson.Gson
import controllers.ChannelsController
import io.ktor.http.cio.websocket.*
import models.Subscribe
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.*

class SecureMessengerServer(
    private val gson: Gson,
    private val channelsController: ChannelsController
) {
    val usersCounter = AtomicInteger()
    val memberNames = ConcurrentHashMap<String, String>()
    val members = ConcurrentHashMap<SecureMessengerSession, WebSocketSession>()
    val lastMessages = LinkedList<String>()

    suspend fun memberJoin(session: SecureMessengerSession, socket: WebSocketSession) {
        //val name = memberNames.computeIfAbsent(member) { "user${usersCounter.incrementAndGet()}" }

        members.computeIfPresent(session) { _, _ -> socket }
        members.computeIfAbsent(session) { socket }

        /* val messages = synchronized(lastMessages) { lastMessages.toList() }
         for (message in messages) {
             socket.send(Frame.Text(message))
         }*/
    }

    suspend fun memberLeft(session: SecureMessengerSession, socket: WebSocketSession) {
        /* val connections = members[member]
         connections?.remove(socket)

         if (connections != null && connections.isEmpty()) {
             val name = memberNames.remove(member) ?: member
             broadcast("server", "Member left: $name.")
         }*/
    }

    /* suspend fun sendTo(recipient: String, sender: String, message: String) {
         members[recipient]?.send(Frame.Text("[$sender] $message"))
     }
 */
    suspend fun message(sender: String, message: String) {
        val name = memberNames[sender] ?: sender
        val formatted = "[$name] $message"

        broadcast(formatted)

        synchronized(lastMessages) {
            lastMessages.add(formatted)
            if (lastMessages.size > 100) {
                lastMessages.removeFirst()
            }
        }
    }

    suspend fun onMessageReceived(session: SecureMessengerSession, frame: Frame) {
        if (frame is Frame.Text) {
            val subscribe = gson.fromJson(frame.readText(), Subscribe::class.java)
            channelsController.subscribe(members[session]!!, subscribe)
        }
    }

    private suspend fun broadcast(message: String) {
        members.values.forEach { socket ->
            socket.send(Frame.Text(message))
        }
    }

    private suspend fun broadcast(sender: String, message: String) {
        val name = memberNames[sender] ?: sender
        broadcast("[$name] $message")
    }
}