import {Move} from "./move";

export class MoveState {
  line: Array<Move>
  score: number
  finalState: string

  constructor(line: Array<Move>, score: number, finalState: string) {
    this.line = line;
    this.score = score;
    this.finalState = finalState;
  }
}
