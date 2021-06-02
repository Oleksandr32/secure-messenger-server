package server

import models.User

interface ServerResultsListener {

    suspend fun onLogin(session: SecureMessengerSession, user: User)

    suspend fun onResult(session: SecureMessengerSession, result: Any)

    suspend fun onResult(userId: Int, result: Any)
}