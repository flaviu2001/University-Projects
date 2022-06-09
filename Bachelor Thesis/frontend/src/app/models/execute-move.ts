import {Board} from "./board";
import {Move} from "./move";

export class ExecuteMove {
  boardDto: Board
  moveDto: Move
  choice: number


  constructor(board: Board, move: Move, choice: number) {
    this.boardDto = board;
    this.moveDto = move;
    this.choice = choice;
  }
}
