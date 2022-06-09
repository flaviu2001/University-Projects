package ro.ubb.flaviu.models.pieces

import ro.ubb.flaviu.models.Board
import ro.ubb.flaviu.models.Color
import ro.ubb.flaviu.models.Direction
import ro.ubb.flaviu.models.Position
import ro.ubb.flaviu.models.moves.BasicMove
import ro.ubb.flaviu.models.moves.Move
import java.lang.Double.max

abstract class Piece(var position: Position, var color: Color, val pieceName: PieceName) {
    abstract val value: Double

    abstract fun getValidMoves(board: Board): List<Move>

    abstract fun clone(): Piece

    protected fun getMovesFromDirectionList(board: Board, directions: List<Direction>): List<Move> {
        val validMoves = mutableListOf<Move>()
        for (direction in directions) {
            var currentPosition = position
            currentPosition += direction
            while (currentPosition.valid()) {
                val lookupPiece = board.pieceAt(currentPosition)
                if (lookupPiece != null) {
                    if (lookupPiece.color != color)
                        validMoves.add(BasicMove(color, position, currentPosition, true, max((lookupPiece.value - value) * 10, value - lookupPiece.value)))
                    break
                }
                validMoves.add(BasicMove(color, position, currentPosition, false, 0.0))
                currentPosition += direction
            }
        }
        return validMoves
    }

    protected fun getMovesFromListOfMoves(board: Board, directions: List<Direction>): List<Move> {
        val validMoves = mutableListOf<Move>()
        for (direction in directions) {
            val currentPosition = position + direction
            val lookupPiece = board.pieceAt(currentPosition)
            if (lookupPiece != null && lookupPiece.color == color)
                continue
            val moveScore = if (lookupPiece == null) 0.0 else max((lookupPiece.value - value) * 10, value - lookupPiece.value)
            if (currentPosition.valid())
                validMoves.add(BasicMove(color, position, currentPosition, lookupPiece != null, moveScore))
        }
        return validMoves
    }
}
