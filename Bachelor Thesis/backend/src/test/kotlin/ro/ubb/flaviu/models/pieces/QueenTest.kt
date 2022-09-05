package ro.ubb.flaviu.models.pieces

import ro.ubb.flaviu.models.Board
import ro.ubb.flaviu.models.Color
import ro.ubb.flaviu.models.PieceSet
import ro.ubb.flaviu.models.Position
import ro.ubb.flaviu.models.moves.BasicMove
import ro.ubb.flaviu.models.moves.Move
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class QueenTest {
    private val emptyBoard = Board(Color.WHITE)
    private val boardWithTwoPieces = Board(
        Color.WHITE, PieceSet(
            Bishop(Position(2, 2), Color.WHITE),
            Queen(Position(4, 4), Color.BLACK)
        )
    )

    @Test
    fun getValidMovesMiddle() {
        val queen = Queen(Position(3, 3), Color.WHITE)
        val moves = queen.getValidMoves(emptyBoard).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (position in listOf(
            Position(2, 2),
            Position(1, 1),
            Position(0, 0),
            Position(4, 4),
            Position(5, 5),
            Position(6, 6),
            Position(7, 7),
            Position(2, 4),
            Position(1, 5),
            Position(0, 6),
            Position(4, 2),
            Position(5, 1),
            Position(6, 0),
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
        val queen = Queen(Position(3, 3), Color.WHITE)
        val moves = queen.getValidMoves(boardWithTwoPieces).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (position in listOf(
            Position(4, 4),
            Position(2, 4),
            Position(1, 5),
            Position(0, 6),
            Position(4, 2),
            Position(5, 1),
            Position(6, 0),
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
}