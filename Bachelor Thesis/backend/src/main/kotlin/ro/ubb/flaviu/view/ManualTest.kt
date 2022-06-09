package ro.ubb.flaviu.view

import ro.ubb.flaviu.algorithm.MinMax
import ro.ubb.flaviu.models.Board
import java.io.File

fun main() {
    val beginTime = System.nanoTime()
    val boardString = File("src/main/resources/manual_run.txt").readText().strip()
    val board = Board.stringToBoard(boardString)
    val bestMove = MinMax(board).getBestMoveIterativeDeepening()
    println(bestMove.finalState)
    println("Score: ${bestMove.score}\n")
    if (bestMove.line.isNotEmpty()) {
        for (move in bestMove.line) {
            print(board.pieceAt(move.initialPosition))
            print(" ")
            println(move)
            board.move(move)
            println(board.boardToString())
            println(MinMax(board).evaluate())
        }
    } else {
        println("Already finished")
    }
    val endTime = System.nanoTime()
    println("Elapsed time: ${(endTime-beginTime)/1e9}")
}