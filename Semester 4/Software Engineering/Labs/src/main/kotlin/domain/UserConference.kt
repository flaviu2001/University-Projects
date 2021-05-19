package domain

enum class Role {
    AUTHOR,
    CHAIR,
    CO_CHAIR,
    REVIEWER,
    SC_MEMBER,
    SESSION_CHAR,
    LISTENER,
    SPEAKER
}

data class UserConference (val id: Int, val userId: Int, val conferenceId: Int, val role: Role, var paid: Boolean)