package ro.ubb.flaviu.models.moves

import ro.ubb.flaviu.models.Color
import ro.ubb.flaviu.models.Position

interface Move {
    val initialPosition: Position
    val finalPosition: Position
    val color: Color
    val isCapture: Boolean
    val score: Double
}
