package domain

enum class Availability {
    PLEASED,
    OK,
    REFUSE
}

data class PCMemberProposal (val pcMemberId: Int,
                             val proposalId: Int,
                             val availability: Availability,
                             val assigned: Boolean,
                             val pcMemberUsername: String ="",
                             val proposalTitle: String ="",
                             )
{
    override fun toString(): String {
        return "Reviewer: $pcMemberUsername -- Title: $proposalTitle -- Availability: $availability -- Assigned: $assigned"
    }
}

