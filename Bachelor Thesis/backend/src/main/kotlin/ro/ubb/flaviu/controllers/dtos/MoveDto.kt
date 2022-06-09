package ro.ubb.flaviu.controllers.dtos

import ro.ubb.flaviu.models.Color
import ro.ubb.flaviu.models.Position
import ro.ubb.flaviu.models.moves.BasicMove
import ro.ubb.flaviu.models.moves.Move

data class MoveDto (
    val initialPosition: Position = Position(0, 0),
    val finalPosition: Position = Position(0, 0),
    val color: String = "WHITE",
) {
    companion object {
        fun fromMove(move: Move): MoveDto {
            return MoveDto(move.initialPosition, move.finalPosition, move.color.toString())
        }
    }

    fun toMove(): Move {
        return BasicMove(Color.valueOf(color), initialPosition, finalPosition, false, 0.0)
    }
}
