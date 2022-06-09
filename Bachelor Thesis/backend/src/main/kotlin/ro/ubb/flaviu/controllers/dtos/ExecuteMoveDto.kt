package ro.ubb.flaviu.controllers.dtos

data class ExecuteMoveDto(val boardDto: BoardDto = BoardDto(), val moveDto: MoveDto = MoveDto(), val choice: Int = 0)