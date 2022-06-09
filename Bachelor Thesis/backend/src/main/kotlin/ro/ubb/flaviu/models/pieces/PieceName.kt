package ro.ubb.flaviu.models.pieces

enum class PieceName {
    PAWN,
    KING,
    BISHOP,
    KNIGHT,
    ROOK,
    QUEEN;

    companion object{
        fun getPromotionChoices(): List<PieceName> = listOf(
            BISHOP,
            KNIGHT,
            ROOK,
            QUEEN
        )
    }
}