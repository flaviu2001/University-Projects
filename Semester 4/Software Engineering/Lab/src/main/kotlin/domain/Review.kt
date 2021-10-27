package domain


enum class ReviewResult{
    STRONGLY_REFUSE,
    REFUSE,
    WEAK_REFUSE,
    BORDERLINE,
    WEAK_ACCEPT,
    ACCEPT,
    STRONGLY_ACCEPT,
}

data class Review(val pcMemberId: Int, val proposalId: Int, val reviewResult: ReviewResult)