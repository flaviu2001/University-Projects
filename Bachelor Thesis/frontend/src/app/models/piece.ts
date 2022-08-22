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

export function getPieceImage(piece: Piece): string {
  let pieceLetter = ""
  if (piece.pieceName == "PAWN")
    pieceLetter = "P"
  if (piece.pieceName == "BISHOP")
    pieceLetter = "B"
  if (piece.pieceName == "KING")
    pieceLetter = "K"
  if (piece.pieceName == "KNIGHT")
    pieceLetter = "N"
  if (piece.pieceName == "QUEEN")
    pieceLetter = "Q"
  if (piece.pieceName == "ROOK")
    pieceLetter = "R"
  let colorLetter = ""
  if (piece.color == "WHITE")
    colorLetter = "w"
  else colorLetter = "b"
  return `assets/cburnett/${colorLetter}${pieceLetter}.svg`
}
