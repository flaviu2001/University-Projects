package ro.ubb.flaviu.models.pieces

import ro.ubb.flaviu.models.Board
import ro.ubb.flaviu.models.Color
import ro.ubb.flaviu.models.PieceSet
import ro.ubb.flaviu.models.Position
import ro.ubb.flaviu.models.moves.BasicMove
import ro.ubb.flaviu.models.moves.Move
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class RookTest {
    private val emptyBoard = Board(Color.WHITE)
    private val boardWithTwoPieces = Board(
        Color.WHITE, PieceSet(
            Bishop(Position(3, 5), Color.WHITE),
            Queen(Position(2, 3), Color.BLACK)
        )
    )

    @Test
    fun getValidMovesMiddle() {
        val rook = Rook(Position(3, 3), Color.WHITE)
        val moves = rook.getValidMoves(emptyBoard).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (position in listOf(
            Position(2, 3),
            Position(1, 3),
            Position(0, 3),
            Position(4, 3),
            Position(5, 3),
            Position(6, 3),
            Position(7, 3),
            Position(3, 0),
            Position(3, 1),
            Position(3, 2),
            Position(3, 4),
            Position(3, 5),
            Position(3, 6),
            Position(3, 7),
        ))
            expectedMoves.add(BasicMove(Color.WHITE, Position(3, 3), position, false, 0.0))
        assertEquals(moves, expectedMoves.toSet())
    }

    @Test
    fun getValidMovesOccupiedCells() {
        val rook = Rook(Position(3, 3), Color.WHITE)
        val moves = rook.getValidMoves(boardWithTwoPieces).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (position in listOf(
            Position(2, 3),
            Position(4, 3),
            Position(5, 3),
            Position(6, 3),
            Position(7, 3),
            Position(3, 0),
            Position(3, 1),
            Position(3, 2),
            Position(3, 4),
        ))
            expectedMoves.add(BasicMove(Color.WHITE, Position(3, 3), position, false, 0.0))
        assertEquals(moves, expectedMoves.toSet())
    }
}