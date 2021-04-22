package server

import models.User

data class SecureMessengerSession(val id: String, var user: User? = null)
