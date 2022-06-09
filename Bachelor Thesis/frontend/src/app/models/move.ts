import {Position} from "./position";

export class Move {
  initialPosition: Position
  finalPosition: Position
  color: String
  className: string


  constructor(initialPosition: Position, finalPosition: Position, color: String, className: string) {
    this.initialPosition = initialPosition;
    this.finalPosition = finalPosition;
    this.color = color;
    this.className = className;
  }
}
