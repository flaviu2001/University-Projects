package ro.ubb.flaviu.models.pieces

import models.*
import models.moves.BasicMove
import models.moves.CastlingMove
import models.moves.HistoryMove
import models.moves.Move
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class KingTest {
    private val emptyBoard = Board(Color.WHITE)
    private val boardWithKingsAndRooksWhite = Board(
        Color.WHITE, PieceSet(
            King(Position(0, 4), Color.WHITE),
            Rook(Position(0, 7), Color.WHITE),
            Rook(Position(0, 0), Color.WHITE),
            King(Position(7, 4), Color.BLACK),
            Rook(Position(7, 7), Color.BLACK),
            Rook(Position(7, 0), Color.BLACK),
        )
    )
    private val boardWithKingsAndRooksBlack = Board(
        Color.BLACK, PieceSet(
            King(Position(0, 4), Color.WHITE),
            Rook(Position(0, 7), Color.WHITE),
            Rook(Position(0, 0), Color.WHITE),
            King(Position(7, 4), Color.BLACK),
            Rook(Position(7, 7), Color.BLACK),
            Rook(Position(7, 0), Color.BLACK),
        )
    )
    private val boardWithKingsAndOneRook = Board(
        Color.WHITE, PieceSet(
            King(Position(0, 4), Color.WHITE),
            Rook(Position(0, 7), Color.WHITE),
        )
    )
    private val boardWithKingsAndRooksAndHistory = Board(
        Color.WHITE, PieceSet(
            King(Position(0, 4), Color.WHITE),
            Rook(Position(0, 7), Color.WHITE),
            Rook(Position(0, 0), Color.WHITE),
        ),
        mutableListOf(
            HistoryMove(BasicMove(Color.WHITE, Position(0, 7), Position(0, 6)), PieceName.ROOK),
            HistoryMove(BasicMove(Color.WHITE, Position(0, 6), Position(0, 7)), PieceName.ROOK)
        )
    )
    private val boardWithKingsAndRooksAndPiecesInBetween = Board(
        Color.WHITE, PieceSet(
            King(Position(0, 4), Color.WHITE),
            Rook(Position(0, 7), Color.WHITE),
            Rook(Position(0, 0), Color.WHITE),
            Knight(Position(0, 2), Color.WHITE),
        )
    )

    @Test
    fun getValidMovesMiddleOfBoard() {
        val king = King(Position(3, 3), Color.WHITE)
        val moves = king.getValidMoves(emptyBoard).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (position in listOf(
            Position(2, 2),
            Position(2, 3),
            Position(2, 4),
            Position(3, 2),
            Position(3, 4),
            Position(4, 2),
            Position(4, 3),
            Position(4, 4),
        ))
            expectedMoves.add(BasicMove(Color.WHITE, Position(3, 3), position))
        assertEquals(moves, expectedMoves.toSet())
    }

    @Test
    fun getValidMovesAllCastlingWhite() {
        val king = boardWithKingsAndRooksWhite.pieceAt(Position(0, 4))!!
        val moves = king.getValidMoves(boardWithKingsAndRooksWhite).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (position in listOf(
            Position(0, 5),
            Position(0, 3),
            Position(1, 3),
            Position(1, 4),
            Position(1, 5),
        ))
            expectedMoves.add(BasicMove(Color.WHITE, king.position, position))
        expectedMoves.add(CastlingMove(Color.WHITE, CastleType.KING_SIDE))
        expectedMoves.add(CastlingMove(Color.WHITE, CastleType.QUEEN_SIDE))
        assertEquals(moves, expectedMoves.toSet())
    }

    @Test
    fun getValidMovesAllCastlingBlack() {
        val king = boardWithKingsAndRooksBlack.pieceAt(Position(7, 4))!!
        val moves = king.getValidMoves(boardWithKingsAndRooksBlack).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (position in listOf(
            Position(7, 5),
            Position(7, 3),
            Position(6, 3),
            Position(6, 4),
            Position(6, 5),
        ))
            expectedMoves.add(BasicMove(Color.BLACK, king.position, position))
        expectedMoves.add(CastlingMove(Color.BLACK, CastleType.KING_SIDE))
        expectedMoves.add(CastlingMove(Color.BLACK, CastleType.QUEEN_SIDE))
        assertEquals(moves, expectedMoves.toSet())
    }

    @Test
    fun getValidMovesKingSideOnly() {
        val king = boardWithKingsAndRooksWhite.pieceAt(Position(0, 4))!!
        val moves = king.getValidMoves(boardWithKingsAndOneRook).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (position in listOf(
            Position(0, 5),
            Position(0, 3),
            Position(1, 3),
            Position(1, 4),
            Position(1, 5),
        ))
            expectedMoves.add(BasicMove(Color.WHITE, king.position, position))
        expectedMoves.add(CastlingMove(Color.WHITE, CastleType.KING_SIDE))
        assertEquals(moves, expectedMoves.toSet())
    }

    @Test
    fun getValidMovesQueenSideOnlyDueToHistory() {
        val king = boardWithKingsAndRooksWhite.pieceAt(Position(0, 4))!!
        val moves = king.getValidMoves(boardWithKingsAndRooksAndHistory).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (position in listOf(
            Position(0, 5),
            Position(0, 3),
            Position(1, 3),
            Position(1, 4),
            Position(1, 5),
        ))
            expectedMoves.add(BasicMove(Color.WHITE, king.position, position))
        expectedMoves.add(CastlingMove(Color.WHITE, CastleType.QUEEN_SIDE))
        assertEquals(moves, expectedMoves.toSet())
    }

    @Test
    fun getValidMovesKingSideOnlyDueToPieceInBetween() {
        val king = boardWithKingsAndRooksWhite.pieceAt(Position(0, 4))!!
        val moves = king.getValidMoves(boardWithKingsAndRooksAndPiecesInBetween).toSet()
        val expectedMoves = mutableListOf<Move>()
        for (position in listOf(
            Position(0, 5),
            Position(0, 3),
            Position(1, 3),
            Position(1, 4),
            Position(1, 5),
        ))
            expectedMoves.add(BasicMove(Color.WHITE, king.position, position))
        expectedMoves.add(CastlingMove(Color.WHITE, CastleType.KING_SIDE))
        assertEquals(moves, expectedMoves.toSet())
    }
}