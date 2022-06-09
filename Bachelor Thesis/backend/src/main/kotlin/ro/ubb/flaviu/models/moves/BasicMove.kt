package ro.ubb.flaviu.models.moves

import ro.ubb.flaviu.models.Color
import ro.ubb.flaviu.models.Position

data class BasicMove(
    override val color: Color,
    override val initialPosition: Position,
    override val finalPosition: Position,
    override val isCapture: Boolean,
    override val score: Double,
) : Move {
}

