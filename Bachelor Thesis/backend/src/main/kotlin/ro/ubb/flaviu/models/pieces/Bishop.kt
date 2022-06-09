package ro.ubb.flaviu.models.pieces

import ro.ubb.flaviu.models.Board
import ro.ubb.flaviu.models.Color
import ro.ubb.flaviu.models.Direction
import ro.ubb.flaviu.models.Position
import ro.ubb.flaviu.models.moves.Move

class Bishop(position: Position, color: Color): Piece(position, color, PieceName.BISHOP) {
    override val value: Double
        get() = 3.0
    override fun getValidMoves(board: Board): List<Move> {
        val directions = mutableListOf<Direction>()
        for (i in listOf<Byte>(-1, 1))
            for (j in listOf<Byte>(-1, 1))
                directions.add(Direction(i, j))
        return getMovesFromDirectionList(board, directions)
    }

    override fun clone(): Piece {
        return Bishop(position, color)
    }
}
