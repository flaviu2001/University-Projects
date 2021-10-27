package domain

data class Session(val sessionId: Int, val conferenceId: Int, val topic: String, val participantsLimit: Int, val roomId: Int = -1) {
    override fun toString(): String {
        return topic
    }
}
