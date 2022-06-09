import {Component, Input, OnInit} from '@angular/core';
import {Position} from "../../models/position";
import {Piece} from "../../models/piece";

@Component({
  selector: 'app-chess-piece',
  templateUrl: './chess-piece.component.html',
  styleUrls: ['./chess-piece.component.sass']
})
export class ChessPieceComponent implements OnInit {
  // @ts-ignore
  @Input() position: Position
  @Input() piece: Piece | null = null
  @Input() moveTo: boolean = false

  constructor() { }

  ngOnInit(): void {
  }

  pieceColor(): string {
    if ((this.position.rank + this.position.file)%2 == 0)
      return "#b58863"
    return "#f0d9b5"
  }

  pieceImage(): string {
    if (this.piece == null)
      return ""
    let pieceLetter = ""
    if (this.piece.pieceName == "PAWN")
      pieceLetter = "P"
    if (this.piece.pieceName == "BISHOP")
      pieceLetter = "B"
    if (this.piece.pieceName == "KING")
      pieceLetter = "K"
    if (this.piece.pieceName == "KNIGHT")
      pieceLetter = "N"
    if (this.piece.pieceName == "QUEEN")
      pieceLetter = "Q"
    if (this.piece.pieceName == "ROOK")
      pieceLetter = "R"
    let colorLetter = ""
    if (this.piece.color == "WHITE")
      colorLetter = "w"
    else colorLetter = "b"
    return `assets/cburnett/${colorLetter}${pieceLetter}.svg`
  }

  validPiece(): boolean {
    return this.piece != null
  }
}
