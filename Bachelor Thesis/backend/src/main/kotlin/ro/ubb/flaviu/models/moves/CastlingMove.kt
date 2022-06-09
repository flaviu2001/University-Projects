package ro.ubb.flaviu.models.moves

import ro.ubb.flaviu.models.CastleType
import ro.ubb.flaviu.models.Color
import ro.ubb.flaviu.models.Position

data class CastlingMove(override val color: Color, val castleType: CastleType): Move {
    override val score: Double
        get() = 100.0

    override val isCapture: Boolean = false

    override val initialPosition: Position
        get() = when(color) {
                Color.WHITE -> Position(0, 4)
                Color.BLACK -> Position(7, 4)
        }

    override val finalPosition: Position
        get() = when(color) {
            Color.WHITE -> when(castleType) {
                CastleType.KING_SIDE -> Position(0, 7)
                CastleType.QUEEN_SIDE -> Position(0, 0)
            }
            Color.BLACK -> when(castleType) {
                CastleType.KING_SIDE -> Position(7, 7)
                CastleType.QUEEN_SIDE -> Position(7, 0)
            }
        }
}
