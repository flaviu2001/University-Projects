package domain

enum class Role {
    AUTHOR,
    CHAIR,
    CO_CHAIR,
    REVIEWER,
    SC_MEMBER,
    LISTENER,
    SPEAKER,
    SESSION_CHAIR,
}

data class UserConference (val id: Int, val userId: Int, val conferenceId: Int, val role: Role, var paid: Boolean)