package ro.ubb.flaviu.models.moves

import ro.ubb.flaviu.models.Color
import ro.ubb.flaviu.models.Position
import ro.ubb.flaviu.models.pieces.PieceName

data class PromotionMove(
    override val color: Color,
    override val initialPosition: Position,
    val file: Byte,
    val promotionChoice: PieceName,
    override val isCapture: Boolean
) : Move {
    override val finalPosition: Position
        get() {
            var rank = 7.toByte()
            if (color == Color.BLACK)
                rank = 0
            return Position(rank, file)
        }

    override val score: Double
        get() = when(promotionChoice) {
            PieceName.QUEEN -> 1000.0
            else -> -100.0
        }
}
