package domain

data class Proposal(
    var id: Int,
    var userConferenceId: Int,
    var abstractText: String,
    var paperText: String,
    var title: String,
    var authors: String,
    var keywords: String,
    var finalized: Boolean,
    var accepted: Boolean
) {
    override fun toString(): String {
        return title
    }
}