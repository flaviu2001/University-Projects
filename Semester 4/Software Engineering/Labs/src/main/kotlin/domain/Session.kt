package domain

data class Session(val sessionId: Int, val conferenceId: Int, val topic: String) {
    override fun toString(): String {
        return topic
    }
}
