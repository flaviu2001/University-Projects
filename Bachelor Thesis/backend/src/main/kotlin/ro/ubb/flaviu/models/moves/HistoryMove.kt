package ro.ubb.flaviu.models.moves

import ro.ubb.flaviu.models.pieces.Piece
import ro.ubb.flaviu.models.pieces.PieceName

data class HistoryMove(val move: Move, val initialPieceName: PieceName, val attackedPiece: Piece?)
