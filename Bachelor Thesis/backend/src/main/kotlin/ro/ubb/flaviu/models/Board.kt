package ro.ubb.flaviu.models

import ro.ubb.flaviu.exceptions.InvalidMoveException
import ro.ubb.flaviu.exceptions.InvalidMoveExceptionCode
import ro.ubb.flaviu.models.moves.*
import ro.ubb.flaviu.models.pieces.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class Board(
    var currentColor: Color,
    val pieceSet: PieceSet = PieceSet(),
    val historyOfMoves: MutableList<HistoryMove> = mutableListOf()
) {
    companion object {
        val zobristTable: List<List<Int>> = List(64) {
            List(12) {
                Random.nextInt()
            }
        }
        val zobristBlackToMove = Random.nextInt()

        fun getStartingBoard(): Board {
            return Board(Color.WHITE, PieceSet.getStartingPieceSet())
        }

        fun stringToBoard(boardString: String): Board {
            val splitString = boardString.split("\n")
            val color = if (splitString.last() == "white") Color.WHITE else Color.BLACK
            val pieceSet = PieceSet()
            for ((rank, line) in splitString.subList(0, 8).withIndex())
                for ((file, char) in line.withIndex()) {
                    val trueRank = 7 - rank
                    val position = Position(trueRank.toByte(), file.toByte())
                    val piece = when(char) {
                        'P' -> Pawn(position, Color.WHITE)
                        'B' -> Bishop(position, Color.WHITE)
                        'N' -> Knight(position, Color.WHITE)
                        'K' -> King(position, Color.WHITE)
                        'Q' -> Queen(position, Color.WHITE)
                        'R' -> Rook(position, Color.WHITE)
                        'p' -> Pawn(position, Color.BLACK)
                        'b' -> Bishop(position, Color.BLACK)
                        'n' -> Knight(position, Color.BLACK)
                        'k' -> King(position, Color.BLACK)
                        'q' -> Queen(position, Color.BLACK)
                        'r' -> Rook(position, Color.BLACK)
                        else -> null
                    }
                    piece?.let { pieceSet.put(piece) }
                }
            return Board(color, pieceSet)
        }
    }
    
    private fun removeOnePiece(position: Position): Piece? {
        val eatenPiece = pieceSet[position]
        if (eatenPiece != null) {
            pieceSet.remove(eatenPiece.position)
            return eatenPiece
        }
        return null
    }

    private fun moveOnePiece(movedPiece: Piece, finalPosition: Position): Piece? {
        val attackedPiece = removeOnePiece(finalPosition)
        val initialPosition = movedPiece.position
        movedPiece.position = finalPosition
        pieceSet.put(movedPiece)
        pieceSet.remove(initialPosition)
        return attackedPiece
    }

    internal fun getCastlingRights(color: Color): List<CastleType> {
        val castlingRights = mutableListOf<CastleType>()
        var queenSideRookValid = true
        var kingSideRookValid = true
        for (historyMove in historyOfMoves)
            if (historyMove.move.color == color) {
                if (historyMove.initialPieceName == PieceName.KING || historyMove.move is CastlingMove)
                    return emptyList()
                else if (historyMove.initialPieceName == PieceName.ROOK && historyMove.move.initialPosition.file == 0.toByte())
                    queenSideRookValid = false
                else if (historyMove.initialPieceName == PieceName.ROOK && historyMove.move.initialPosition.file == 7.toByte())
                    kingSideRookValid = false
            }
        val king = when (color) {
            Color.WHITE -> pieceSet[Position(0, 4)]
            Color.BLACK -> pieceSet[Position(7, 4)]
        } ?: return emptyList()
        if (king.pieceName != PieceName.KING)
            return emptyList()
        val queenSideRook: Piece?
        val kingSideRook: Piece?
        if (color == Color.WHITE) {
            queenSideRook = pieceSet[Position(0, 0)]
            kingSideRook = pieceSet[Position(0, 7)]
        } else {
            queenSideRook = pieceSet[Position(7, 0)]
            kingSideRook = pieceSet[Position(7, 7)]
        }
        if (queenSideRook == null || queenSideRook.pieceName != PieceName.ROOK)
            queenSideRookValid = false
        if (kingSideRook == null || kingSideRook.pieceName != PieceName.ROOK)
            kingSideRookValid = false

        fun verifyNoPiecesBetween(initialPosition: Position, direction: Direction): Boolean {
            var currentPosition = initialPosition + direction
            while (currentPosition != king.position) {
                if (pieceSet[currentPosition] != null)
                    return false
                currentPosition += direction
            }
            return true
        }

        fun verifyNoAttacksBetween(kingPosition: Position, rookPosition: Position): Boolean {
            for (piece in pieceSet.values())
                if (piece.color == color.otherColor())
                    if (piece is Pawn || piece is King) {
                        if (abs(piece.position.rank - kingPosition.rank) == 1 &&
                            piece.position.file >= min(kingPosition.file.toInt(), rookPosition.file.toInt())-1 &&
                            piece.position.file <= max(kingPosition.file.toInt(), rookPosition.file.toInt())+1
                        )
                            return false
                    } else if (piece is Bishop || piece is Rook || piece is Queen || piece is Knight) {
                        for (move in piece.getValidMoves(this))
                            if (move.finalPosition.rank == kingPosition.rank &&
                                move.finalPosition.file >= min(kingPosition.file.toInt(), rookPosition.file.toInt()) &&
                                move.finalPosition.file <= max(kingPosition.file.toInt(), rookPosition.file.toInt())) {
                                if (move.finalPosition.file == rookPosition.file)
                                    continue
                                return false
                            }
                    }
            return true
        }

        if (queenSideRookValid && queenSideRook != null && verifyNoPiecesBetween(
                queenSideRook.position,
                Direction(0, 1)
            ) && verifyNoAttacksBetween(king.position, queenSideRook.position)
        )
            castlingRights.add(CastleType.QUEEN_SIDE)
        if (kingSideRookValid && kingSideRook != null && verifyNoPiecesBetween(
                kingSideRook.position,
                Direction(0, -1)
            ) && verifyNoAttacksBetween(king.position, kingSideRook.position)
        )
            castlingRights.add(CastleType.KING_SIDE)
        return castlingRights
    }

    fun pieceAt(position: Position): Piece? {
        return pieceSet[position]
    }

    fun move(move: Move) {
        fun getMovedPiece(): Piece {
            val movedPiece =
                pieceSet[move.initialPosition]
                    ?: throw InvalidMoveException(InvalidMoveExceptionCode.NoPieceAtPosition)
            if (movedPiece.color != currentColor)
                throw InvalidMoveException(InvalidMoveExceptionCode.MovingOtherColorsPiece)
            return movedPiece
        }

        var attackedPiece: Piece? = null
        val movedPieceName: PieceName
        when (move) {
            is BasicMove -> {
                val movedPiece = getMovedPiece()
                attackedPiece = moveOnePiece(movedPiece, move.finalPosition)
                movedPieceName = movedPiece.pieceName
            }
            is CastlingMove -> {
                val rank: Byte = when (move.color) {
                    Color.WHITE -> 0
                    Color.BLACK -> 7
                }
                val king = pieceSet[Position(rank, 4)]
                    ?: throw InvalidMoveException(InvalidMoveExceptionCode.NoPieceAtPosition)
                when (move.castleType) {
                    CastleType.QUEEN_SIDE -> {
                        val rook = pieceSet[Position(rank, 0)]
                            ?: throw InvalidMoveException(InvalidMoveExceptionCode.NoPieceAtPosition)
                        moveOnePiece(king, Position(rank, 2))
                        moveOnePiece(rook, Position(rank, 3))
                    }
                    CastleType.KING_SIDE -> {
                        val rook = pieceSet[Position(rank, 7)]
                            ?: throw InvalidMoveException(InvalidMoveExceptionCode.NoPieceAtPosition)
                        moveOnePiece(king, Position(rank, 6))
                        moveOnePiece(rook, Position(rank, 5))
                    }
                }
                movedPieceName = PieceName.KING
            }
            is EnPassantMove -> {
                val movedPiece = getMovedPiece()
                moveOnePiece(movedPiece, move.finalPosition)
                val directionTowardsEatenPawn = when (currentColor) {
                    Color.WHITE -> Direction(-1, 0)
                    Color.BLACK -> Direction(1, 0)
                }
                attackedPiece = removeOnePiece(move.finalPosition + directionTowardsEatenPawn)
                movedPieceName = PieceName.PAWN
            }
            is PromotionMove -> {
                val rank: Byte = when (move.color) {
                    Color.WHITE -> 7
                    Color.BLACK -> 0
                }
                val finalPosition = Position(rank, move.file)
                attackedPiece = moveOnePiece(getMovedPiece(), finalPosition)
                val newPiece = when (move.promotionChoice) {
                    PieceName.BISHOP -> Bishop(finalPosition, currentColor)
                    PieceName.KNIGHT -> Knight(finalPosition, currentColor)
                    PieceName.QUEEN -> Queen(finalPosition, currentColor)
                    PieceName.ROOK -> Rook(finalPosition, currentColor)
                    else -> throw InvalidMoveException(InvalidMoveExceptionCode.UnpromotablePieceType)
                }
                removeOnePiece(finalPosition)
                pieceSet.put(newPiece)
                movedPieceName = PieceName.PAWN
            }
            else -> throw InvalidMoveException(InvalidMoveExceptionCode.InvalidMoveType)
        }
        historyOfMoves.add(HistoryMove(move, movedPieceName, attackedPiece))
        currentColor = currentColor.otherColor()
    }

    fun unmove() {
        val historyMove = historyOfMoves.removeLast() // Can throw error
        when (val move = historyMove.move) {
            is BasicMove -> {
                val movedPiece =
                    pieceSet[move.finalPosition] ?: throw InvalidMoveException(InvalidMoveExceptionCode.NoPieceAtPosition)
                moveOnePiece(movedPiece, move.initialPosition)
                historyMove.attackedPiece?.let { pieceSet.put(it) }
            }
            is CastlingMove -> {
                val rank: Byte = when (move.color) {
                    Color.WHITE -> 0
                    Color.BLACK -> 7
                }
                val king = pieceSet[Position(rank, if (move.castleType == CastleType.QUEEN_SIDE) 2 else 6)]
                    ?: throw InvalidMoveException(InvalidMoveExceptionCode.NoPieceAtPosition)
                val rook = pieceSet[Position(rank, if (move.castleType == CastleType.QUEEN_SIDE) 3 else 5)]
                    ?: throw InvalidMoveException(InvalidMoveExceptionCode.NoPieceAtPosition)
                moveOnePiece(king, Position(rank, 4))
                when (move.castleType) {
                    CastleType.QUEEN_SIDE -> {
                        moveOnePiece(rook, Position(rank, 0))
                    }
                    CastleType.KING_SIDE -> {
                        moveOnePiece(rook, Position(rank, 7))
                    }
                }
            }
            is EnPassantMove -> {
                val movedPiece = pieceSet[move.finalPosition] ?: throw InvalidMoveException(InvalidMoveExceptionCode.NoPieceAtPosition)
                moveOnePiece(movedPiece, move.initialPosition)
                historyMove.attackedPiece!!.let { pieceSet.put(it) }
            }
            is PromotionMove -> {
                removeOnePiece(move.finalPosition)
                pieceSet.put(Pawn(move.initialPosition, move.color))
                historyMove.attackedPiece?.let { pieceSet.put(it) }
            }
            else -> throw InvalidMoveException(InvalidMoveExceptionCode.InvalidMoveType)
        }
        currentColor = currentColor.otherColor()
    }

    fun getAllValidMoves(checkDanger: Boolean = false): List<Move> {
        val validMoves = mutableListOf<Move>()
        for (piece in pieceSet.values()) {
            if (piece.color == currentColor)
                for (move in piece.getValidMoves(this)) {
                    if (checkDanger) {
                        val clonedBoard = clone()
                        clonedBoard.move(move)
                        if (!clonedBoard.isKingInDanger(clonedBoard.getAllValidMoves(false), currentColor))
                            validMoves.add(move)
                    } else validMoves.add(move)
                }
        }
        return validMoves
    }

    // moves is the list of moves that can be made in this board state
    fun isKingInDanger(moves: List<Move>, color: Color): Boolean {
        var kingPosition: Position? = null
        for (piece in pieceSet.values()) {
            if (piece is King && piece.color == color)
                kingPosition = piece.position
        }
        if (kingPosition == null) {
            return true // How is this possible?
        }
        for (move in moves) {
            if (move is BasicMove || move is EnPassantMove || move is PromotionMove)
                if (move.finalPosition == kingPosition)
                    return true
        }
        return false
    }

    fun getState(): BoardState {
        val moves = getAllValidMoves()
        if (currentColor == Color.WHITE && isKingInDanger(moves, Color.BLACK))
            return BoardState.WHITE_WIN
        if (currentColor == Color.BLACK && isKingInDanger(moves, Color.WHITE))
            return BoardState.BLACK_WIN
        if (moves.isEmpty())
            return BoardState.DRAW
        return BoardState.UNFINISHED
    }

    private fun isKingInActualDanger(color: Color): Boolean {
        val newBoard = this.clone()
        newBoard.currentColor = newBoard.currentColor.otherColor()
        var kingPosition: Position? = null
        for (piece in newBoard.pieceSet.values()) {
            if (piece is King && piece.color == color)
                kingPosition = piece.position
        }
        if (kingPosition == null) {
            return true // How is this possible?
        }
        for (move in newBoard.getAllValidMoves(false)) {
            if (move is BasicMove || move is EnPassantMove || move is PromotionMove)
                if (move.finalPosition == kingPosition)
                    return true
        }
        return false
    }

    fun getStateActualState(): BoardState {
        val moves = getAllValidMoves(true)
        if (moves.isNotEmpty())
            return BoardState.UNFINISHED
        if (currentColor == Color.WHITE && isKingInActualDanger(Color.WHITE))
            return BoardState.BLACK_WIN
        if (currentColor == Color.BLACK && isKingInActualDanger(Color.BLACK))
            return BoardState.WHITE_WIN
        return BoardState.DRAW
    }

    fun boardToString(printColor: Boolean = true): String {
        val stringifiedBoard = MutableList(8) { MutableList(8) {"."} }

        for (piece in pieceSet.values()) {
            var symbol = when (piece) {
                is Pawn -> "P"
                is Bishop -> "B"
                is Knight -> "N"
                is King -> "K"
                is Queen -> "Q"
                else -> "R"
            }
            if (piece.color == Color.BLACK)
                symbol = symbol.lowercase()
            stringifiedBoard[piece.position.rank.toInt()][piece.position.file.toInt()] = symbol
        }

        var stringToReturn = stringifiedBoard.reversed().reduce{ current, new ->
            val joinedList = current.toMutableList()
            joinedList.add("\n")
            joinedList.addAll(new)
            joinedList
        }.reduce{ subCurrent, subNew ->
            "$subCurrent$subNew"
        }
        if (printColor)
            stringToReturn = stringToReturn.plus(if (currentColor == Color.WHITE) "\nwhite" else "\nblack")

        return stringToReturn
    }

    fun zobristHash(): Int {
        var hash = 0
        for (piece in pieceSet.values()) {
            val secondIndex = when (piece.pieceName) {
                PieceName.PAWN -> 0
                PieceName.QUEEN -> 1
                PieceName.BISHOP -> 2
                PieceName.KING -> 3
                PieceName.KNIGHT -> 4
                PieceName.ROOK -> 5
            } + if (piece.color == Color.BLACK) 6 else 0
            hash = hash xor zobristTable[piece.position.rank * 8 + piece.position.file][secondIndex]
        }
        if (currentColor == Color.BLACK)
            hash = hash xor zobristBlackToMove
        return hash
    }

    override fun hashCode(): Int {
        return zobristHash()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Board) {
            return currentColor == other.currentColor && pieceSet == other.pieceSet
        }
        return false
    }

    fun clone(): Board {
        return Board(currentColor, pieceSet.clone(), historyOfMoves.toMutableList())
    }
}
