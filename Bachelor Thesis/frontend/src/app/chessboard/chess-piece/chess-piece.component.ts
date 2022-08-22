import {Component, Input, OnInit} from '@angular/core';
import {Position} from "../../models/position";
import {getPieceImage, Piece} from "../../models/piece";

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
  @Input() lastMove: boolean = false

  constructor() { }

  ngOnInit(): void {
  }

  pieceColor(): string {
    if (this.lastMove)
      return "#295cb9"
    if ((this.position.rank + this.position.file)%2 == 0)
      return "#6a6a6a"
    return "#e5e5e5"
  }

  pieceImage(): string {
    if (this.piece == null)
      return ""
    return getPieceImage(this.piece)
  }

  validPiece(): boolean {
    return this.piece != null
  }
}
