package controllers

import models.Subscribe
import repositories.UsersRepository
import server.SecureMessengerSession
import server.ServerResultsListener

class UsersController(private val usersRepository: UsersRepository) : Subscribable {

    companion object {
        private const val SEARCH_ACTION = "search"
    }

    override suspend fun subscribe(
        listener: ServerResultsListener,
        session: SecureMessengerSession,
        subscribe: Subscribe
    ) {
        when (subscribe.action) {
            SEARCH_ACTION -> {
                val query = subscribe.data!!.value as String
                val users = usersRepository.getUsers(query).filter { it.id != subscribe.userId }
                listener.onResult(session, users)
            }
            else -> {
            }
        }
    }
}