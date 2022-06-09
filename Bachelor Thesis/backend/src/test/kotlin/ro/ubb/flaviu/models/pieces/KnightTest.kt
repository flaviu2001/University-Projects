package ro.ubb.flaviu.models.pieces

import models.Board
import models.Color
import models.PieceSet
import models.Position
import models.moves.BasicMove
import models.moves.Move
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class KnightTest {

    private val emptyBoard = Board(Color.WHITE)
    private val boardWithTwoPieces = Board(Color.WHITE, PieceSet(
        Bishop(Position(2, 1), Color.WHITE),
        Queen(Position(1, 2), Color.BLACK)
    )
    )

    @Test
    fun getValidMovesMiddleOfBoard() {
        val knight = Knight(Position(3, 3), Color.WHITE)
        val moves = knight.getValidMoves(emptyBoard).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (position in listOf(
            Position(2, 5),
            Position(2, 1),
            Position(4, 5),
            Position(4, 1),
            Position(1, 2),
            Position(1, 4),
            Position(5, 2),
            Position(5, 4)
        ))
            expectedMoves.add(BasicMove(Color.WHITE, Position(3, 3), position))
        assertEquals(moves, expectedMoves.toSet())
    }

    @Test
    fun getValidMovesCorner() {
        val knight = Knight(Position(7, 7), Color.WHITE)
        val moves = knight.getValidMoves(emptyBoard).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (position in listOf(
            Position(6, 5),
            Position(5, 6)
        ))
            expectedMoves.add(BasicMove(Color.WHITE, Position(7, 7), position))
        assertEquals(moves, expectedMoves.toSet())
    }

    @Test
    fun getValidMovesOccupiedCells() {
        val knight = Knight(Position(3, 3), Color.WHITE)
        val moves = knight.getValidMoves(boardWithTwoPieces).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (position in listOf(
            Position(2, 5),
            Position(4, 5),
            Position(4, 1),
            Position(1, 2),
            Position(1, 4),
            Position(5, 2),
            Position(5, 4)
        ))
            expectedMoves.add(BasicMove(Color.WHITE, Position(3, 3), position))
        assertEquals(moves, expectedMoves.toSet())
    }
}