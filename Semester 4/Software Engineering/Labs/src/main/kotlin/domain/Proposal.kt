package domain

data class Proposal(
    val id: Int,
    val userConferenceId: Int,
    val abstractText: String,
    val paperText: String,
    val title: String,
    val authors: String,
    val keywords: String,
    val accepted: Boolean
) {
    override fun toString(): String {
        return title
    }
}