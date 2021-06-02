package controllers

import com.google.gson.Gson
import models.*
import repositories.UsersRepository
import server.SecureMessengerSession
import server.ServerResultsListener

class AuthController(
    private val gson: Gson,
    private val usersRepository: UsersRepository
) : Subscribable {

    companion object {
        private const val LOGIN_ACTION = "login"
        private const val REGISTER_ACTION = "register"
    }

    override suspend fun subscribe(
        listener: ServerResultsListener,
        session: SecureMessengerSession,
        subscribe: Subscribe
    ) {
        when (subscribe.action) {
            LOGIN_ACTION -> {
                val json = gson.toJson(subscribe.data?.value)
                val requestUser = gson.fromJson(json, User::class.java)
                val user = usersRepository.getUser(requestUser.userName!!)
                if (user != null && user.password == requestUser.password) {
                    val updatedUser = user.apply { publicKey = user.publicKey ?: publicKey }
                    usersRepository.updateUser(updatedUser)
                    listener.onLogin(session, updatedUser!!)
                    listener.onResult(session, updatedUser)
                }
            }
            REGISTER_ACTION -> {
                val json = gson.toJson(subscribe.data?.value)
                val user = gson.fromJson(json, User::class.java)
                usersRepository.saveUser(user)
                listener.onLogin(session, user)
                listener.onResult(session, user)
            }
            else -> {
            }
        }
    }
}