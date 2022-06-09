package ro.ubb.flaviu.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ro.ubb.flaviu.algorithm.MinMax
import ro.ubb.flaviu.controllers.dtos.BoardDto
import ro.ubb.flaviu.controllers.dtos.ExecuteMoveDto
import ro.ubb.flaviu.controllers.dtos.MoveDto
import ro.ubb.flaviu.models.Board
import ro.ubb.flaviu.models.BoardState
import ro.ubb.flaviu.models.moves.PromotionMove
import ro.ubb.flaviu.models.pieces.PieceName

@RestController
class Controller {
    @GetMapping("/start")
    fun getStartingBoardWhite(): BoardDto {
        return BoardDto.fromBoard(Board.getStartingBoard())
    }

    @PostMapping("/compute")
    fun computeMove(@RequestBody boardDto: BoardDto): BoardDto {
        val board = boardDto.toBoard()
        val cloneBoard = board.clone()
        val moveState = MinMax(board).getBestMoveIterativeDeepening()
        if (moveState.line.isNotEmpty())
            cloneBoard.move(moveState.line[0])
        return BoardDto.fromBoard(cloneBoard)
    }

    @PostMapping("/move")
    fun move(@RequestBody executeMoveDto: ExecuteMoveDto): BoardDto {
        val board = executeMoveDto.boardDto.toBoard()
        for (move in board.getAllValidMoves(true))
            if (move.initialPosition == executeMoveDto.moveDto.initialPosition && move.finalPosition == executeMoveDto.moveDto.finalPosition) {
                if (move is PromotionMove) {
                    if ((move.promotionChoice == PieceName.KNIGHT && executeMoveDto.choice == 1) ||
                        (move.promotionChoice == PieceName.BISHOP && executeMoveDto.choice == 2) ||
                        (move.promotionChoice == PieceName.ROOK && executeMoveDto.choice == 3) ||
                        (move.promotionChoice == PieceName.QUEEN && executeMoveDto.choice == 4)) {
                        board.move(move)
                        break
                    }
                }
                else {
                    board.move(move)
                    break
                }
            }
        return BoardDto.fromBoard(board)
    }

    @PostMapping("/getmoves")
    fun getMoves(@RequestBody boardDto: BoardDto): List<MoveDto> {
        return boardDto.toBoard().getAllValidMoves(true).map { MoveDto.fromMove(it) }
    }

    @PostMapping("/getstate")
    fun getState(@RequestBody boardDto: BoardDto): BoardState {
        return boardDto.toBoard().getState()
    }
}
