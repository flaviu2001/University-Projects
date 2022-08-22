import {Board} from "./board";
import {Move} from "./move";

export class ExecuteMove {
  moveDto: Move
  choice: number


  constructor(move: Move, choice: number) {
    this.moveDto = move;
    this.choice = choice;
  }
}
