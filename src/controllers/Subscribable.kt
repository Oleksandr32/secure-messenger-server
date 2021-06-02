package controllers

import io.ktor.http.cio.websocket.*
import models.Subscribe
import server.SecureMessengerSession
import server.ServerResultsListener

interface Subscribable {

    suspend fun subscribe(listener: ServerResultsListener, session: SecureMessengerSession, subscribe: Subscribe)
}