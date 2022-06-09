package ro.ubb.flaviu.models.pieces

import ro.ubb.flaviu.models.*
import ro.ubb.flaviu.models.moves.BasicMove
import ro.ubb.flaviu.models.moves.CastlingMove
import ro.ubb.flaviu.models.moves.Move

class King(position: Position, color: Color): Piece(position, color, PieceName.KING) {
    override val value: Double
        get() = 0.0

    override fun getValidMoves(board: Board): List<Move> {
        val directions = mutableListOf<Direction>()
        for (i in listOf<Byte>(-1, 0, 1))
            for (j in listOf<Byte>(-1, 0, 1))
                if (i != 0.toByte() || j != 0.toByte())
                    directions.add(Direction(i, j))
        val validMoves = getMovesFromListOfMoves(board, directions).map {
            BasicMove(it.color, it.initialPosition, it.finalPosition, it.isCapture, -100.0) as Move
        }.toMutableList()
        val castlingRights = board.getCastlingRights(color)
        for (castlingRight in castlingRights)
            validMoves.add(CastlingMove(color, castlingRight))
        return validMoves
    }

    override fun clone(): Piece {
        return King(position, color)
    }
}
