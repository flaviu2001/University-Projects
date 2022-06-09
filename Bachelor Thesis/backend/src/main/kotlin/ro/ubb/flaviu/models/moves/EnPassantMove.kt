package ro.ubb.flaviu.models.moves

import ro.ubb.flaviu.models.Color
import ro.ubb.flaviu.models.Position

data class EnPassantMove(
    override val color: Color,
    override val initialPosition: Position,
    override val finalPosition: Position,
) : Move {
    override val isCapture: Boolean = false
    override val score: Double
        get() = 200.0
}
