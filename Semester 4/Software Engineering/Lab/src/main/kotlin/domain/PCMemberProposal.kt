package domain

enum class Availability {
    PLEASED,
    OK,
    REFUSE
}

data class PCMemberProposal (val pcMemberId: Int, val proposalId: Int, val availability: Availability, val assigned: Boolean)