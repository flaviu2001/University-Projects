package ro.ubb.flaviu.models

import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import ro.ubb.flaviu.models.pieces.*
import ro.ubb.flaviu.models.moves.*

internal class BoardTest {

    private val boardBasicMoveNonEating = Board(
        Color.WHITE, PieceSet(Rook(Position(1, 3), Color.WHITE))
    )

    private val boardBasicMoveEating = Board(
        Color.WHITE, PieceSet(
            Rook(Position(1, 3), Color.WHITE),
            Rook(Position(5, 3), Color.BLACK)
        )
    )

    private val boardCastlingMove = Board(
        Color.WHITE, PieceSet(
            King(Position(0, 4), Color.WHITE),
            Rook(Position(0, 7), Color.WHITE),
            Rook(Position(0, 0), Color.WHITE),
            King(Position(7, 4), Color.BLACK),
            Rook(Position(7, 7), Color.BLACK),
            Rook(Position(7, 0), Color.BLACK),
        )
    )

    private val boardEnPassantMove = Board(
        Color.WHITE, PieceSet(
            Pawn(Position(4, 3), Color.WHITE),
            Pawn(Position(4, 4), Color.BLACK)
        ),
        mutableListOf(HistoryMove(BasicMove(Color.BLACK, Position(6, 4), Position(4, 4), false, 0.0), PieceName.PAWN, null))
    )

    private val boardPromotionMove = Board(
        Color.WHITE, PieceSet(
            Pawn(Position(6, 5), Color.WHITE),
            Queen(Position(7, 6), Color.BLACK)
        )
    )

    private fun piecesEqual(piece1: Piece?, piece2: Piece?): Boolean {
        if (piece1 == null && piece2 == null)
            return true
        if (piece1 == null || piece2 == null)
            return false
        return piece1.color == piece2.color && piece1.position == piece2.position && piece1.pieceName == piece2.pieceName
    }

    @Test
    fun moveBasicMoveNonEating() {
        boardBasicMoveNonEating.move(BasicMove(Color.WHITE, Position(1, 3), Position(4, 3), false, 0.0))
        assert(piecesEqual(boardBasicMoveNonEating.pieceAt(Position(4, 3)), Rook(Position(4, 3), Color.WHITE)))
        assertNull(boardBasicMoveNonEating.pieceAt(Position(1, 3)))
    }

    @Test
    fun moveBasicMoveEating() {
        boardBasicMoveEating.move(BasicMove(Color.WHITE, Position(1, 3), Position(5, 3), true, 0.0))
        assert(piecesEqual(boardBasicMoveEating.pieceAt(Position(5, 3)), Rook(Position(5, 3), Color.WHITE)))
        assertNull(boardBasicMoveEating.pieceAt(Position(1, 3)))
        assert(boardBasicMoveEating.historyOfMoves.size == 1)
    }

    @Test
    fun moveCastlingMove() {
        boardCastlingMove.move(CastlingMove(Color.WHITE, CastleType.KING_SIDE))
        assert(piecesEqual(boardCastlingMove.pieceAt(Position(0, 6)), King(Position(0, 6), Color.WHITE)))
        assert(piecesEqual(boardCastlingMove.pieceAt(Position(0, 5)), Rook(Position(0, 5), Color.WHITE)))
        assert(piecesEqual(boardCastlingMove.pieceAt(Position(0, 0)), Rook(Position(0, 0), Color.WHITE)))
        assertNull(boardCastlingMove.pieceAt(Position(0, 7)))
        assertNull(boardCastlingMove.pieceAt(Position(0, 4)))
    }

    @Test
    fun moveEnPassantMove() {
        boardEnPassantMove.move(EnPassantMove(Color.WHITE, Position(4, 3), Position(5, 4)))
        assert(piecesEqual(boardEnPassantMove.pieceAt(Position(5, 4)), Pawn(Position(5, 4), Color.WHITE)))
        assertNull(boardEnPassantMove.pieceAt(Position(4, 4)))
    }

    @Test
    fun movePromotionMove() {
        boardPromotionMove.move(PromotionMove(Color.WHITE, Position(6, 5), 6, PieceName.ROOK, false))
        assert(piecesEqual(boardPromotionMove.pieceAt(Position(7, 6)), Rook(Position(7, 6), Color.WHITE)))
        assertNull(boardPromotionMove.pieceAt(Position(6, 5)))
    }
}
