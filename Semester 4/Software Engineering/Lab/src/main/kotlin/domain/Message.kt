package domain

class Message  (val text: String,
                val proposalId: Int,
                val userId: Int,
                val userName: String,
                ) {
    override fun toString(): String {
        return "$userName: $text"
    }
}
