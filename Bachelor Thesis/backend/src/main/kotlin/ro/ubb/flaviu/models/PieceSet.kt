package ro.ubb.flaviu.models

import ro.ubb.flaviu.models.pieces.*

class PieceSet(vararg pieces: Piece) {
    companion object {
        fun getStartingPieceSet(): PieceSet {
            val pieceList = mutableListOf<Piece>()
            for (file in 0..7) {
                pieceList.add(Pawn(Position(1, file.toByte()), Color.WHITE))
                pieceList.add(Pawn(Position(6, file.toByte()), Color.BLACK))
            }
            pieceList.add(Rook(Position(0, 0), Color.WHITE))
            pieceList.add(Rook(Position(0, 7), Color.WHITE))
            pieceList.add(Rook(Position(7, 0), Color.BLACK))
            pieceList.add(Rook(Position(7, 7), Color.BLACK))

            pieceList.add(Knight(Position(0, 1), Color.WHITE))
            pieceList.add(Knight(Position(0, 6), Color.WHITE))
            pieceList.add(Knight(Position(7, 1), Color.BLACK))
            pieceList.add(Knight(Position(7, 6), Color.BLACK))

            pieceList.add(Bishop(Position(0, 2), Color.WHITE))
            pieceList.add(Bishop(Position(0, 5), Color.WHITE))
            pieceList.add(Bishop(Position(7, 2), Color.BLACK))
            pieceList.add(Bishop(Position(7, 5), Color.BLACK))

            pieceList.add(Queen(Position(0, 3), Color.WHITE))
            pieceList.add(Queen(Position(7, 3), Color.BLACK))
            pieceList.add(King(Position(0, 4), Color.WHITE))
            pieceList.add(King(Position(7, 4), Color.BLACK))

            return PieceSet(*pieceList.toTypedArray())
        }
    }

    private val pieceSet = mutableMapOf<Position, Piece>()

    init {
        for (piece in pieces)
            pieceSet[piece.position] = piece
    }

    fun put(piece: Piece) {
        pieceSet[piece.position] = piece
    }

    fun remove(position: Position) {
        pieceSet.remove(position)
    }

    operator fun get(position: Position): Piece? {
        return pieceSet[position]
    }

    fun values(): Iterable<Piece> {
        return pieceSet.map { (_, value) ->
            value
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is PieceSet) {
            return pieceSet == other.pieceSet
        }
        return false
    }

    override fun hashCode(): Int {
        return pieceSet.hashCode()
    }

    fun clone(): PieceSet {
        val pieceList = mutableListOf<Piece>()
        for (entry in pieceSet) {
            pieceList.add(entry.value.clone())
        }
        return PieceSet(*pieceList.toTypedArray())
    }
}