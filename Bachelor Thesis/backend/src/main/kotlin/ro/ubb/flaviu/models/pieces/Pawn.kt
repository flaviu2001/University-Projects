package ro.ubb.flaviu.models.pieces

import ro.ubb.flaviu.models.Board
import ro.ubb.flaviu.models.Color
import ro.ubb.flaviu.models.Direction
import ro.ubb.flaviu.models.Position
import ro.ubb.flaviu.models.moves.BasicMove
import ro.ubb.flaviu.models.moves.EnPassantMove
import ro.ubb.flaviu.models.moves.Move
import ro.ubb.flaviu.models.moves.PromotionMove
import java.lang.Double.max
import kotlin.math.abs

class Pawn(position: Position, color: Color) : Piece(position, color, PieceName.PAWN) {
    override val value: Double
        get() = 1.0
    override fun getValidMoves(board: Board): List<Move> {
        val validMoves = mutableListOf<Move>()
        val directionAlongFile: Direction
        val enPassantRank: Byte
        val postEnPassantRank: Byte
        val homeRank: Byte
        val prePromotionRank: Byte
        val promotionRank: Byte
        when (color) {
            Color.WHITE -> {
                homeRank = 1
                prePromotionRank = 6
                promotionRank = 7
                enPassantRank = 4
                postEnPassantRank = 5
                directionAlongFile = Direction(1, 0)
            }
            Color.BLACK -> {
                homeRank = 6
                prePromotionRank = 1
                promotionRank = 0
                enPassantRank = 3
                postEnPassantRank = 2
                directionAlongFile = Direction(-1, 0)
            }
        }
        val basicPawnMove = BasicMove(color, position, position + directionAlongFile, false, 0.0)
        if (board.pieceAt(basicPawnMove.finalPosition) == null && position.rank != prePromotionRank) {
            validMoves.add(basicPawnMove)
            val doubleStepMove = BasicMove(color, position, position + directionAlongFile + directionAlongFile, false, 10.0)
            if (position.rank == homeRank && board.pieceAt(doubleStepMove.finalPosition) == null)
                validMoves.add(doubleStepMove)
        }

        val leftOrRightAttacks = listOf<Byte>(-1, 1).map {
            position + directionAlongFile + Direction(0, it)
        }
        for (newPosition in leftOrRightAttacks) {
            val lookupPiece = board.pieceAt(newPosition)
            if (newPosition.valid() && lookupPiece != null && lookupPiece.color != color)
                validMoves.add(BasicMove(color, position, newPosition, true, max((lookupPiece.value - value) * 10, value - lookupPiece.value)))
        }

        validMoves.removeAll(validMoves.filter { it.finalPosition.rank == promotionRank })

        if (position.rank == prePromotionRank) {
            if (board.pieceAt(position + directionAlongFile) == null)
                for (promotionChoice in PieceName.getPromotionChoices())
                    validMoves.add(PromotionMove(color, position, position.file, promotionChoice, false))
            val leftOrRightPromotions = listOf<Byte>(-1, 1).map {
                position + directionAlongFile + Direction(0, it)
            }
            for (newPosition in leftOrRightPromotions)
                if (newPosition.valid() && board.pieceAt(newPosition) != null && board.pieceAt(newPosition)!!.color != color)
                    for (promotionChoice in PieceName.getPromotionChoices())
                        validMoves.add(PromotionMove(color, position, newPosition.file, promotionChoice, true))
        }
        if (position.rank == enPassantRank) {
            if (board.historyOfMoves.size == 0)
                return validMoves
            val lastMove = board.historyOfMoves.last()
            if (lastMove.move.color == color)
                return validMoves
            if (lastMove.initialPieceName != PieceName.PAWN || lastMove.move !is BasicMove)
                return validMoves
            if (abs(lastMove.move.finalPosition.rank - lastMove.move.initialPosition.rank) != 2)
                return validMoves
            if (abs(lastMove.move.finalPosition.file - position.file) != 1)
                return validMoves
            validMoves.add(
                EnPassantMove(
                    color,
                    position,
                    Position(postEnPassantRank, lastMove.move.finalPosition.file)
                )
            )
        }
        return validMoves
    }

    override fun clone(): Piece {
        return Pawn(position, color)
    }
}