package ro.ubb.flaviu.models.pieces

import models.Board
import models.Color
import models.PieceSet
import models.Position
import models.moves.BasicMove
import models.moves.Move
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class BishopTest {
    private val emptyBoard = Board(Color.WHITE)
    private val boardWithTwoPieces = Board(
        Color.WHITE, PieceSet(
            Bishop(Position(2, 2), Color.WHITE),
            Queen(Position(4, 4), Color.BLACK)
        )
    )

    @Test
    fun getValidMovesMiddle() {
        val bishop = Bishop(Position(3, 3), Color.WHITE)
        val moves = bishop.getValidMoves(emptyBoard).toSet()
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
        ))
            expectedMoves.add(BasicMove(Color.WHITE, Position(3, 3), position))
        assertEquals(moves, expectedMoves.toSet())
    }

    @Test
    fun getValidMovesOccupiedCells() {
        val bishop = Bishop(Position(3, 3), Color.WHITE)
        val moves = bishop.getValidMoves(boardWithTwoPieces).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (position in listOf(
            Position(4, 4),
            Position(2, 4),
            Position(1, 5),
            Position(0, 6),
            Position(4, 2),
            Position(5, 1),
            Position(6, 0),
        ))
            expectedMoves.add(BasicMove(Color.WHITE, Position(3, 3), position))
        assertEquals(moves, expectedMoves.toSet())
    }
}