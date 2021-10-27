package domain

import java.sql.Date

data class ProposalSession(val proposalId: Int, val sessionId: Int, val time: Date) {
    override fun toString(): String {
        return "$proposalId at time $time"
    }
}
