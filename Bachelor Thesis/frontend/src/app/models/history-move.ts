import {Move} from "./move";

export class HistoryMove {
  move: Move
  initialPieceName: string


  constructor(move: Move, initialPieceName: string) {
    this.move = move;
    this.initialPieceName = initialPieceName;
  }
}
