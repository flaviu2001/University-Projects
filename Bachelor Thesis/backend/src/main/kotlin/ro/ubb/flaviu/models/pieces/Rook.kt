package ro.ubb.flaviu.models.pieces

import ro.ubb.flaviu.models.Board
import ro.ubb.flaviu.models.Color
import ro.ubb.flaviu.models.Direction
import ro.ubb.flaviu.models.Position
import ro.ubb.flaviu.models.moves.Move

class Rook(position: Position, color: Color): Piece(position, color, PieceName.ROOK) {
    override val value: Double
        get() = 5.0
    override fun getValidMoves(board: Board): List<Move> {
        val directions = mutableListOf<Direction>()
        for (i in listOf<Byte>(-1, 1)) {
            directions.add(Direction(i, 0))
            directions.add(Direction(0, i))
        }
        return getMovesFromDirectionList(board, directions)
    }

    override fun clone(): Piece {
        return Rook(position, color)
    }
}
