package ro.ubb.flaviu.controllers.dtos

import ro.ubb.flaviu.models.Board
import ro.ubb.flaviu.models.BoardState
import ro.ubb.flaviu.models.Color
import ro.ubb.flaviu.models.PieceSet

data class BoardDto(val pieceList: List<PieceDto> = emptyList(), val currentColor: String = "WHITE", val historyOfMoves: List<HistoryMoveDto> = emptyList(), val state: String = "UNFINISHED") {
    companion object {
        fun fromBoard(board: Board): BoardDto {
            return BoardDto(
                board.pieceSet.values().map { PieceDto.fromPiece(it) },
                board.currentColor.toString(),
                board.historyOfMoves.map{HistoryMoveDto.fromHistoryMove(it) },
                board.getStateActualState().toString()
            )
        }
    }

    fun toBoard(): Board {
        return Board(
            Color.valueOf(currentColor),
            PieceSet(*this.pieceList.map { it.toPiece() }.toTypedArray()),
            historyOfMoves.map { it.toHistoryMove() }.toMutableList(),
        )
    }
}
