package ro.ubb.flaviu.controllers.dtos

import ro.ubb.flaviu.models.moves.HistoryMove
import ro.ubb.flaviu.models.pieces.Piece
import ro.ubb.flaviu.models.pieces.PieceName

data class HistoryMoveDto (val moveDto: MoveDto = MoveDto(), val initialPieceName: String = "PAWN", val attackedPiece: Piece? = null) {
    companion object {
        fun fromHistoryMove(historyMove: HistoryMove): HistoryMoveDto {
            return HistoryMoveDto(MoveDto.fromMove(historyMove.move), historyMove.initialPieceName.toString())
        }
    }

    fun toHistoryMove(): HistoryMove {
        return HistoryMove(moveDto.toMove(), PieceName.valueOf(initialPieceName), attackedPiece)
    }
}
