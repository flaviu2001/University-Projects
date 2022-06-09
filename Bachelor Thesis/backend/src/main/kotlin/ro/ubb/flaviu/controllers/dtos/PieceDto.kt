package ro.ubb.flaviu.controllers.dtos

import ro.ubb.flaviu.models.Color
import ro.ubb.flaviu.models.Position
import ro.ubb.flaviu.models.pieces.*

data class PieceDto (var position: Position = Position(0, 0), var color: String = "WHITE", val pieceName: String = "PAWN") {
    companion object {
        fun fromPiece(piece: Piece): PieceDto {
            return PieceDto(piece.position, piece.color.toString(), piece.pieceName.toString())
        }
    }

    fun toPiece(): Piece {
        return when(PieceName.valueOf(pieceName)) {
            PieceName.PAWN -> Pawn(position, Color.valueOf(color))
            PieceName.BISHOP -> Bishop(position, Color.valueOf(color))
            PieceName.KING -> King(position, Color.valueOf(color))
            PieceName.KNIGHT -> Knight(position, Color.valueOf(color))
            PieceName.QUEEN -> Queen(position, Color.valueOf(color))
            PieceName.ROOK -> Rook(position, Color.valueOf(color))
        }
    }
}
