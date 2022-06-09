package ro.ubb.flaviu.models.pieces

import models.Board
import models.Color
import models.PieceSet
import models.Position
import models.moves.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class PawnTest {
    private val boardWithWhitePawn = Board(
        Color.WHITE, PieceSet(Pawn(Position(1, 3), Color.WHITE))
    )
    private val boardWithBlackPawn = Board(
        Color.BLACK, PieceSet(Pawn(Position(6, 3), Color.BLACK))
    )
    private val boardWithPawnInTheMiddle = Board(
        Color.WHITE, PieceSet(Pawn(Position(2, 3), Color.WHITE))
    )
    private val boardWithPawnWithEnPassant = Board(
        Color.WHITE, PieceSet(
            Pawn(Position(4, 3), Color.WHITE),
            Pawn(Position(4, 4), Color.BLACK)
        ),
        mutableListOf(HistoryMove(BasicMove(Color.BLACK, Position(6, 4), Position(4, 4)), PieceName.PAWN))
    )
    private val boardWithPawnPromoting = Board(
        Color.BLACK, PieceSet(
            Pawn(Position(1, 6), Color.BLACK)
        )
    )
    private val boardWithPawnPromotingTwoPlaces = Board(
        Color.BLACK, PieceSet(
            Pawn(Position(1, 6), Color.BLACK),
            Queen(Position(0, 5), Color.WHITE)
        )
    )

    @Test
    fun getValidMovesOneAndTwoStepsWhite() {
        val pawn = boardWithWhitePawn.pieceAt(Position(1, 3))!!
        val moves = pawn.getValidMoves(boardWithWhitePawn).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (position in listOf(
            Position(2, 3),
            Position(3, 3),
        ))
            expectedMoves.add(BasicMove(Color.WHITE, Position(1, 3), position))
        assertEquals(moves, expectedMoves.toSet())
    }

    @Test
    fun getValidMovesOneAndTwoStepsBlack() {
        val pawn = boardWithBlackPawn.pieceAt(Position(6, 3))!!
        val moves = pawn.getValidMoves(boardWithBlackPawn).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (position in listOf(
            Position(5, 3),
            Position(4, 3),
        ))
            expectedMoves.add(BasicMove(Color.BLACK, Position(6, 3), position))
        assertEquals(moves, expectedMoves.toSet())
    }

    @Test
    fun getValidMovesOneStepWhite() {
        val pawn = boardWithPawnInTheMiddle.pieceAt(Position(2, 3))!!
        val moves = pawn.getValidMoves(boardWithPawnInTheMiddle).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (position in listOf(
            Position(3, 3)
        ))
            expectedMoves.add(BasicMove(Color.WHITE, Position(2, 3), position))
        assertEquals(moves, expectedMoves.toSet())
    }

    @Test
    fun getValidMovesEnPassant() {
        val pawn = boardWithPawnWithEnPassant.pieceAt(Position(4, 3))!!
        val moves = pawn.getValidMoves(boardWithPawnWithEnPassant).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (position in listOf(
            Position(5, 3)
        ))
            expectedMoves.add(BasicMove(Color.WHITE, Position(4, 3), position))
        expectedMoves.add(EnPassantMove(Color.WHITE, Position(4, 3), Position(5, 4)))
        assertEquals(moves, expectedMoves.toSet())
    }

    @Test
    fun getValidMovesWithPromotion() {
        val pawn = boardWithPawnPromoting.pieceAt(Position(1, 6))!!
        val moves = pawn.getValidMoves(boardWithPawnPromoting).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (promotionChoice in listOf(PieceName.ROOK, PieceName.QUEEN, PieceName.BISHOP, PieceName.KNIGHT))
            expectedMoves.add(PromotionMove(Color.BLACK, Position(1, 6), 6, promotionChoice))
        assertEquals(moves, expectedMoves.toSet())
    }

    @Test
    fun getValidMovesWithPromotionInTwoPlaces() {
        val pawn = boardWithPawnPromotingTwoPlaces.pieceAt(Position(1, 6))!!
        val moves = pawn.getValidMoves(boardWithPawnPromotingTwoPlaces).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (promotionChoice in listOf(PieceName.ROOK, PieceName.QUEEN, PieceName.BISHOP, PieceName.KNIGHT)) {
            expectedMoves.add(PromotionMove(Color.BLACK, Position(1, 6), 6, promotionChoice))
            expectedMoves.add(PromotionMove(Color.BLACK, Position(1, 6), 5, promotionChoice))
        }
        assertEquals(moves, expectedMoves.toSet())
    }
}
