import {Position} from "./position";

export class Piece {
  position: Position
  color: string
  pieceName: string

  constructor(position: Position, color: string, pieceName: string) {
    this.position = position;
    this.color = color;
    this.pieceName = pieceName;
  }
}
