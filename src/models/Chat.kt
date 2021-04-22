package models

data class Chat(
    val id: Int,
    var me: User,
    var other: User,
    val status: ChatStatus,
    val messages: ArrayList<Message> = arrayListOf()
) {

    fun swapUsers(): Chat {
        val first = me
        me = other
        other = first
        return this
    }
}