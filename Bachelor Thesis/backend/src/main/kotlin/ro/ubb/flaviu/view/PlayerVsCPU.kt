package ro.ubb.flaviu.view

import ro.ubb.flaviu.algorithm.MinMax
import ro.ubb.flaviu.models.Board
import ro.ubb.flaviu.models.BoardState
import ro.ubb.flaviu.models.Color
import ro.ubb.flaviu.models.moves.Move
import ro.ubb.flaviu.models.pieces.Piece

fun main() {
    val board = Board.getStartingBoard()
    print("Player color(white/black): ")
    val playerColor = if (readLine() == "white") Color.WHITE else Color.BLACK
    var lastCPUMove: Move? = null
    var lastCPUPieceMoved : Piece? = null
    var lastMoveDepth = 0

    while (board.getState() == BoardState.UNFINISHED) {
        if (board.currentColor == playerColor) {
            print("\u001b[H\u001b[2J")
            if (lastCPUMove != null) {
                println("depth: $lastMoveDepth - $lastCPUPieceMoved - $lastCPUMove")
                println()
            }

            println(board.boardToString(false))
            println()
            val moves = board.getAllValidMoves(true)
            for ((index, move) in moves.withIndex()) {
                println("$index: ${board.pieceAt(move.initialPosition)} - $move")
            }
            println()
            if (moves.isEmpty()) {
                val moveState = MinMax(board).getBestMoveIterativeDeepening()
                println(moveState.finalState)
                break
            }


            var index: Int
            while (true) {
                print("Move index: ")
                try {
                    index = readLine()!!.toInt()
                    if (index >= 0 && index < moves.size)
                        break
                    println("bad number")
                } catch (e: NumberFormatException) {
                    println("bad number")
                }
            }

            board.move(moves[index])
        } else {
            val moveState = MinMax(board).getBestMoveIterativeDeepening()
            if (moveState.line.isNotEmpty()) {
                lastCPUMove = moveState.line[0]
                lastCPUPieceMoved = board.pieceAt(moveState.line[0].initialPosition)
                lastMoveDepth = moveState.line.size
                board.move(moveState.line[0])
            }
        }
    }
}
