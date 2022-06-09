package ro.ubb.flaviu.controllers.dtos

import ro.ubb.flaviu.models.BoardState
import ro.ubb.flaviu.models.moves.Move

data class MoveStateDto (val line: List<MoveDto>, val score: Double, val finalState: String)
