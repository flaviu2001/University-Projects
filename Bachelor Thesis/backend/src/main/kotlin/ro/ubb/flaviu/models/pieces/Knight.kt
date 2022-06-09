package ro.ubb.flaviu.models.pieces

import ro.ubb.flaviu.models.Board
import ro.ubb.flaviu.models.Color
import ro.ubb.flaviu.models.Direction
import ro.ubb.flaviu.models.Position
import ro.ubb.flaviu.models.moves.Move

class Knight(position: Position, color: Color): Piece(position, color, PieceName.KNIGHT) {
    override val value: Double
        get() = 3.0
    override fun getValidMoves(board: Board): List<Move> {
        val directions = mutableListOf<Direction>()
        for (i in listOf<Byte>(-2, 2))
            for (j in listOf<Byte>(-1, 1)) {
                directions.add(Direction(i, j))
                directions.add(Direction(j, i))
            }
        return getMovesFromListOfMoves(board, directions)
    }

    override fun clone(): Piece {
        return Knight(position, color)
    }
}