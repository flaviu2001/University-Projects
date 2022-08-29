package ro.ubb.flaviu.algorithm

import ro.ubb.flaviu.models.Board
import ro.ubb.flaviu.models.BoardState
import ro.ubb.flaviu.models.Color
import ro.ubb.flaviu.models.moves.Move
import ro.ubb.flaviu.models.pieces.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.math.max
import kotlin.math.min

data class MoveState(val line: List<Move>, val score: Double, val finalState: BoardState)

class MinMax (private val board: Board,
              val ENABLE_ALPHA_BETA_PRUNING: Boolean = true,
              val ENABLE_QUIESCENCE: Boolean = false,
              val ENABLE_TRANSPOSITION_TABLE: Boolean = true,
              val ENABLE_PARALLELIZATION: Boolean = false,
              val ITERATIVE_DEEPENING_LOW_CUTOFF: Long = 3000,
              val ITERATIVE_DEEPENING_HIGH_CUTOFF: Long = 30000) {
    companion object {
        const val MINIMUM_VALUE = -1000000.0
        const val MAXIMUM_VALUE = 1000000.0
        const val QUIESCENCE_DEPTH = 10
        private const val TRANSPOSITION_TABLE_SIZE = 1e6
        private const val TRANSPOSITION_DEPTH_LIMIT = 100
        const val THREADS_PER_NODE = 3
        const val PARALLELIZATION_DEPTH_CUTOFF = 1
        const val DEBUG = true

        var quiescentNodes = 0
        var allNodes = 0
        var transpositionTable = mutableMapOf<Board, Double>()

    }

    private fun setTranspositionTableValue(board: Board, depth: Int, value: Double) {
        if (ENABLE_TRANSPOSITION_TABLE && transpositionTable.size < TRANSPOSITION_TABLE_SIZE && depth <= TRANSPOSITION_DEPTH_LIMIT) {
            transpositionTable[board] = value
        }
    }

    private fun getTranspositionTableValue(board: Board): Double? {
        return transpositionTable[board]
    }

    fun evaluate(): Double {
        var evaluation = 0.0
        for (piece in board.pieceSet.values()) {
            var worth = when(piece) {
                is Pawn -> 1
                is Bishop -> 3
                is Knight -> 3
                is Rook -> 5
                is Queen -> 9
                else -> 0
            }
            if (piece.color == Color.BLACK)
                worth = -worth
            evaluation += worth
        }
        return evaluation
    }

    private fun quiesce(depthLeft: Int = QUIESCENCE_DEPTH, _alpha: Double = MINIMUM_VALUE, _beta: Double = MAXIMUM_VALUE): Double {
        ++quiescentNodes
        ++allNodes
        var alpha = _alpha
        var beta = _beta

        val boardState = board.getState()
        if (boardState != BoardState.UNFINISHED) {
            return when(boardState) {
                BoardState.WHITE_WIN -> MAXIMUM_VALUE
                BoardState.BLACK_WIN -> MINIMUM_VALUE
                else -> 0.0
            }
        }

        if (depthLeft == 0)
            return evaluate()

        val moves = board.getAllValidMoves().filter { it.isCapture }.shuffled().sortedByDescending { it.score }
        var evaluation = evaluate()

        for (move in moves) {
            board.move(move)
            val score = MinMax(board, ENABLE_ALPHA_BETA_PRUNING, ENABLE_QUIESCENCE, ENABLE_TRANSPOSITION_TABLE, ENABLE_PARALLELIZATION, ITERATIVE_DEEPENING_LOW_CUTOFF).quiesce(depthLeft-1, alpha, beta)
            board.unmove()
            if (board.currentColor == Color.WHITE) {
                if (score > evaluation)
                    evaluation = score
                if (ENABLE_ALPHA_BETA_PRUNING) {
                    alpha = max(alpha, score)
                    if (beta < alpha)
                        return evaluation
                }
            } else {
                if (score < evaluation)
                    evaluation = score

                if (ENABLE_ALPHA_BETA_PRUNING) {
                    beta = min(beta, score)
                    if (beta < alpha)
                        return evaluation
                }
            }
        }

        if (moves.isEmpty())
            evaluation = evaluate()
        return evaluation
    }

    private fun getBestMove(depthLeft: Int, depthDone: Int, _alpha: Double = MINIMUM_VALUE, _beta: Double = MAXIMUM_VALUE): MoveState {
        if (ENABLE_TRANSPOSITION_TABLE) {
            val score = getTranspositionTableValue(board)
            if (score != null)
                return MoveState(emptyList(), score, BoardState.UNFINISHED)
        }
        ++allNodes
        var alpha = _alpha
        var beta = _beta

        val state = board.getState()
        if (state != BoardState.UNFINISHED) {
            if (state == BoardState.WHITE_WIN)
                return MoveState(
                    emptyList(),
                    MAXIMUM_VALUE,
                    BoardState.WHITE_WIN
                )
            if (state == BoardState.BLACK_WIN)
                return MoveState(
                    emptyList(),
                    MINIMUM_VALUE,
                    BoardState.BLACK_WIN
                )
            return MoveState(emptyList(), 0.0, BoardState.DRAW)
        }

        if (depthLeft == 0) {
            if (ENABLE_QUIESCENCE) {
                return MoveState(emptyList(), quiesce(_alpha = alpha, _beta = beta), BoardState.UNFINISHED)
            }

            return MoveState(emptyList(), evaluate(), BoardState.UNFINISHED)
        }

        val moves = board.getAllValidMoves().shuffled().sortedByDescending { it.score }
        var evaluation = if (board.currentColor == Color.WHITE) MINIMUM_VALUE else MAXIMUM_VALUE
        var currentBestLine: List<Move> = mutableListOf()
        var finalState = BoardState.UNFINISHED
        var lastDepth = 10000
        var cntSureMoves = 0
        var b = 0
        val c = moves.size
        if (ENABLE_PARALLELIZATION && depthDone <= PARALLELIZATION_DEPTH_CUTOFF)
            b = min(THREADS_PER_NODE, moves.size/2)
        if (b > 0) {
            val executorService = Executors.newFixedThreadPool(b)
            for (move in moves.slice(0 until b)) {
                executorService.execute {
                    val boardClone = board.clone()
                    boardClone.move(move)
                    val moveState = MinMax(boardClone, ENABLE_ALPHA_BETA_PRUNING, ENABLE_QUIESCENCE, ENABLE_TRANSPOSITION_TABLE, ENABLE_PARALLELIZATION, ITERATIVE_DEEPENING_LOW_CUTOFF).getBestMove(depthLeft-1, depthDone+1, alpha, beta)

                    if (board.currentColor == Color.WHITE && moveState.finalState == BoardState.BLACK_WIN && moveState.line.isEmpty()) {
                        ++cntSureMoves
                        return@execute
                    }
                    if (board.currentColor == Color.BLACK && moveState.finalState == BoardState.WHITE_WIN && moveState.line.isEmpty()) {
                        ++cntSureMoves
                        return@execute
                    }

                    if (board.currentColor == Color.WHITE) {
                        if (moveState.score > evaluation || (evaluation == moveState.score && lastDepth > moveState.line.size)) {
                            evaluation = moveState.score
                            currentBestLine = listOf(move, *moveState.line.toTypedArray())
                            finalState = moveState.finalState
                            lastDepth = moveState.line.size
                        }
                        if (ENABLE_ALPHA_BETA_PRUNING)
                            alpha = max(alpha, moveState.score)
                    } else {
                        if (moveState.score < evaluation || (evaluation == moveState.score && lastDepth > moveState.line.size)) {
                            evaluation = moveState.score
                            currentBestLine = listOf(move, *moveState.line.toTypedArray())
                            finalState = moveState.finalState
                            lastDepth = moveState.line.size
                        }

                        if (ENABLE_ALPHA_BETA_PRUNING)
                            beta = min(beta, moveState.score)
                    }
                }
            }
            executorService.shutdown()
            executorService.awaitTermination(10, TimeUnit.MINUTES)
        }
        for (move in moves.slice(b until c)) {
            board.move(move)
            val moveState = MinMax(board, ENABLE_ALPHA_BETA_PRUNING, ENABLE_QUIESCENCE, ENABLE_TRANSPOSITION_TABLE, ENABLE_PARALLELIZATION, ITERATIVE_DEEPENING_LOW_CUTOFF).getBestMove(depthLeft-1, depthDone+1, alpha, beta)
            board.unmove()

            if (board.currentColor == Color.WHITE && moveState.finalState == BoardState.BLACK_WIN && moveState.line.isEmpty()) {
                ++cntSureMoves
                continue
            }
            if (board.currentColor == Color.BLACK && moveState.finalState == BoardState.WHITE_WIN && moveState.line.isEmpty()) {
                ++cntSureMoves
                continue
            }

            if (board.currentColor == Color.WHITE) {
                if (moveState.score > evaluation || (evaluation == moveState.score && lastDepth > moveState.line.size)) {
                    evaluation = moveState.score
                    currentBestLine = listOf(move, *moveState.line.toTypedArray())
                    finalState = moveState.finalState
                    lastDepth = moveState.line.size
                }
                if (ENABLE_ALPHA_BETA_PRUNING) {
                    alpha = max(alpha, moveState.score)
                    if (beta < alpha) {
                        val moveState2 = MoveState(currentBestLine, evaluation, finalState)
                        setTranspositionTableValue(board, depthLeft, moveState2.score)
                        return moveState2
                    }
                }
            } else {
                if (moveState.score < evaluation || (evaluation == moveState.score && lastDepth > moveState.line.size)) {
                    evaluation = moveState.score
                    currentBestLine = listOf(move, *moveState.line.toTypedArray())
                    finalState = moveState.finalState
                    lastDepth = moveState.line.size
                }

                if (ENABLE_ALPHA_BETA_PRUNING) {
                    beta = min(beta, moveState.score)
                    if (beta < alpha) {
                        val moveState2 = MoveState(currentBestLine, evaluation, finalState)
                        setTranspositionTableValue(board, depthLeft, moveState2.score)
                        return moveState2
                    }
                }
            }
        }

        if (cntSureMoves == moves.size) {
            val cloneBoard = board.clone()
            cloneBoard.currentColor = cloneBoard.currentColor.otherColor()
            if (!cloneBoard.isKingInDanger(cloneBoard.getAllValidMoves(), board.currentColor)) {
                currentBestLine = emptyList()
                evaluation = 0.0
                finalState = BoardState.DRAW
            } else {
                currentBestLine = emptyList()
                if (board.currentColor == Color.WHITE) {
                    evaluation = MINIMUM_VALUE
                    finalState = BoardState.BLACK_WIN
                } else {
                    evaluation = MAXIMUM_VALUE
                    finalState = BoardState.WHITE_WIN
                }
            }
        }

        val moveState2 = MoveState(currentBestLine, evaluation, finalState)
        setTranspositionTableValue(board, depthLeft, moveState2.score)
        return moveState2
    }

    fun getBestMoveIterativeDeepening(): MoveState {
        val start = System.nanoTime()
        var depth = 0
        var moveState: MoveState? = null
        var lastAllNodes = 0
        var lastQuiescentNodes = 0
        transpositionTable = mutableMapOf()
        while(true) {
            quiescentNodes = 0
            allNodes = 0
            transpositionTable = mutableMapOf()

            if (System.nanoTime() - start > ITERATIVE_DEEPENING_LOW_CUTOFF * 1e6.toLong())
                break

            depth++
            val maxMilliseconds = (ITERATIVE_DEEPENING_HIGH_CUTOFF * 1e6.toLong() - System.nanoTime() + start) / 1e6.toLong()

            val executorService = Executors.newFixedThreadPool(1)
            @Suppress("UNCHECKED_CAST")
            val future: Future<MoveState> = executorService.submit( Callable {
                getBestMove(depthLeft = depth, depthDone = 0)
            })

            try {
                moveState = future.get(maxMilliseconds, TimeUnit.MILLISECONDS)
                lastAllNodes = allNodes
                lastQuiescentNodes = quiescentNodes
            } catch (e: TimeoutException) {
                --depth
                break
            } finally {
                executorService.shutdownNow()
            }

            if (DEBUG)
                println("Overall time spent at depth $depth: ${(System.nanoTime() - start) / 1e9} seconds")
        }
        transpositionTable = mutableMapOf()
        if (DEBUG) {
            println(depth)
            println(lastAllNodes)
            println(lastQuiescentNodes)
        }
        return moveState!!
    }
}
