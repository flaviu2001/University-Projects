package ro.ubb.flaviu.controllers

import org.springframework.web.bind.annotation.*
import ro.ubb.flaviu.algorithm.MinMax
import ro.ubb.flaviu.controllers.dtos.BoardDto
import ro.ubb.flaviu.controllers.dtos.ExecuteMoveDto
import ro.ubb.flaviu.controllers.dtos.MoveDto
import ro.ubb.flaviu.controllers.dtos.OptionsDto
import ro.ubb.flaviu.models.Board
import ro.ubb.flaviu.models.moves.PromotionMove
import ro.ubb.flaviu.models.pieces.PieceName
import kotlin.random.Random
import kotlin.random.nextUInt

@RestController
class Controller {
    companion object {
        val games = mutableMapOf<String, Board>()
        val optionsMap = mutableMapOf<String, OptionsDto>()
    }

    @PostMapping("/new-game")
    fun newGame(@RequestBody options: OptionsDto): String {
        var newId = Random.nextUInt()
        while (newId.toString() in games)
            newId = Random.nextUInt()
        games[newId.toString()] = Board.getStartingBoard()
        optionsMap[newId.toString()] = options
        return "\"$newId\""
    }

    @GetMapping("/get-game/{id}")
    fun getGame(@PathVariable id: String): BoardDto {
        val board = games[id] ?: throw Exception("invalid id")
        return BoardDto.fromBoard(board)
    }

    @PostMapping("/compute/{id}")
    fun computeMove(@PathVariable id: String): BoardDto {
        val board = games[id] ?: throw Exception("invalid id")
        val options = optionsMap[id] ?: throw Exception("invalid id")
        val cloneBoard = board.clone()
        println(options)
        val moveState = MinMax(
            board,
            options.alphaBeta,
            options.quiescence,
            options.transposition,
            options.parallelization,
            options.lowCutoff,
            options.highCutoff
        ).getBestMoveIterativeDeepening()
        if (moveState.line.isNotEmpty())
            cloneBoard.move(moveState.line[0])
        games[id] = cloneBoard
        return BoardDto.fromBoard(cloneBoard)
    }

    @PostMapping("/move/{id}")
    fun move(@RequestBody executeMoveDto: ExecuteMoveDto, @PathVariable id: String): BoardDto {
        val board = games[id] ?: throw Exception("invalid id")
        for (move in board.getAllValidMoves(true))
            if (move.initialPosition == executeMoveDto.moveDto.initialPosition && move.finalPosition == executeMoveDto.moveDto.finalPosition) {
                if (move is PromotionMove) {
                    if ((move.promotionChoice == PieceName.KNIGHT && executeMoveDto.choice == 1) ||
                        (move.promotionChoice == PieceName.BISHOP && executeMoveDto.choice == 2) ||
                        (move.promotionChoice == PieceName.ROOK && executeMoveDto.choice == 3) ||
                        (move.promotionChoice == PieceName.QUEEN && executeMoveDto.choice == 4)
                    ) {
                        board.move(move)
                        break
                    }
                } else {
                    board.move(move)
                    break
                }
            }
        games[id] = board
        return BoardDto.fromBoard(board)
    }

    @GetMapping("/get-moves/{id}")
    fun getMoves(@PathVariable id: String): List<MoveDto> {
        val board = games[id] ?: throw Exception("invalid id")
        return board.getAllValidMoves(true).map { MoveDto.fromMove(it) }
    }
}
