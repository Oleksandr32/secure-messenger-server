package models

data class User(
    var id: Int? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var userName: String? = null,
    var password: String? = null,
    var publicKey: String? = null
)