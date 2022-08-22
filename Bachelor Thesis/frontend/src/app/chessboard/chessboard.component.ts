import { Component, Input, OnInit } from '@angular/core';
import { Observable } from "rxjs";

import { MatDialog } from '@angular/material/dialog';

import { Position } from "../models/position";
import { Board } from "../models/board";
import { Move } from "../models/move";
import { ExecuteMove } from "../models/execute-move";
import { getPieceImage, Piece } from "../models/piece";

import { ChessService} from "../chess.service";

import { PromotionChoiceComponent } from "./promotion-choice/promotion-choice.component";

@Component({
  selector: 'app-chessboard',
  templateUrl: './chessboard.component.html',
  styleUrls: ['./chessboard.component.sass']
})
export class ChessboardComponent implements OnInit {
  @Input() gameID: string = ""
  @Input() playerColor: string = "WHITE"
  @Input() computerVsPlayer: boolean = true

  positions: Array<Position> = []

  board: Board | null = null

  activePosition: Position | null = null
  possibleMoves: Array<Move> = []
  canClick: Array<Position> = []

  constructor(private chessService: ChessService, private dialog: MatDialog) { }

  ngOnInit(): void {
    if (this.playerColor == "WHITE")
      this.positions = Array.from({length: 64}, (_, i) => new Position(7-Math.floor(i/8), i%8))
    else
      this.positions = Array.from({length: 64}, (_, i) => new Position(Math.floor(i/8), i%8))

    this.chessService.getGame(this.gameID).subscribe(board => {
      this.board = board
      if (this.playerColor != this.board.currentColor && this.computerVsPlayer) {
        this.chessService.computeMove(this.gameID).subscribe(board2 => {
          this.board = board2
          this.refreshPossibleMoves()
        })
      } else this.refreshPossibleMoves()
    })
  }

  openDialog(): Observable<number> {
    let dialogRef = this.dialog.open(PromotionChoiceComponent, {
      data: { choice: "0", color: "WHITE" }
    });

    return dialogRef.afterClosed()
  }

  refreshPossibleMoves() {
    if (this.board)
      this.chessService.getMoves(this.gameID).subscribe(moves => this.possibleMoves = moves)
  }

  pieceAtPosition(position: Position): Piece | null {
    if (this.board == null)
      return null
    for (let piece of this.board.pieceList) {
      if (Position.equals(piece.position, position))
        return piece
    }
    return null
  }

  refreshClickable() {
    if (this.activePosition == null)
      this.canClick = []
    else {
      this.canClick = []
      for (let move of this.possibleMoves) {
        if (Position.equals(move.initialPosition, this.activePosition))
          this.canClick.push(move.finalPosition)
      }
    }
  }

  isClickable(position: Position): boolean {
    for (let finalPosition of this.canClick)
      if (Position.equals(finalPosition,position))
        return true
    return false
  }

  isFromLastMove(position: Position): boolean {
    if (this.board == null)
      return false
    if (this.board.historyOfMoves.length == 0)
      return false
    let move = this.board.historyOfMoves[this.board.historyOfMoves.length-1].move
    return Position.equals(move.initialPosition, position) || Position.equals(move.finalPosition, position)
  }

  executeMove(move: Move, choice: number): void {
    this.chessService.move(this.gameID, new ExecuteMove(move, choice)).subscribe(newBoard => {
      this.board = newBoard
      this.refreshPossibleMoves()
      if (this.computerVsPlayer && this.board.state == "UNFINISHED")
        this.chessService.computeMove(this.gameID).subscribe(newerBoard => {
          this.board = newerBoard
          this.refreshPossibleMoves()
        })
    })
  }

  clickPosition(position: Position) {
    if (this.computerVsPlayer && this.board!!.currentColor != this.playerColor)
      return

    if (this.activePosition == null)
      this.activePosition = position
    else if (this.activePosition == position) {
      this.activePosition = null
    } else {
      let found = false
      for (let move of this.possibleMoves)
        if (Position.equals(move.initialPosition, this.activePosition) && Position.equals(move.finalPosition, position)) {
          if (this.pieceAtPosition(move.initialPosition)?.pieceName == "PAWN" && ((this.board?.currentColor == "WHITE" && move.finalPosition.rank == 7) || this.board?.currentColor == "BLACK" && move.finalPosition.rank == 0)) {
            this.openDialog().subscribe(choice => {
              if (choice && choice > 0)
                this.executeMove(move, choice)
            })
          } else this.executeMove(move, 0)
          this.activePosition = null
          found = true
          break
        }
      if (!found)
        this.activePosition = position
    }
    this.refreshClickable()
  }

  getStateString(): string {
    if (this.board == null)
      return ""
    if (this.board.state == "UNFINISHED")
      return ""
    if (this.board.state == "WHITE_WIN")
      return "White wins!"
    if (this.board.state == "BLACK_WIN")
      return "Black wins!"
    return "Draw!"
  }

  showState(): boolean {
    return !(this.board == null || this.board.state == "UNFINISHED");
  }

  getMissingPiecesTally(color: string): Array<Piece> {
    if (this.board == null)
      return []

    let queenCount = 1
    let rookCount = 2
    let bishopCount = 2
    let knightCount = 2
    let pawnCount = 8
    for (let piece of this.board.pieceList) {
      if (piece.color != color)
        continue

      if (piece.pieceName == "QUEEN")
        --queenCount
      if (piece.pieceName == "ROOK")
        --rookCount
      if (piece.pieceName == "BISHOP")
        --bishopCount
      if (piece.pieceName == "KNIGHT")
        --knightCount
      if (piece.pieceName == "PAWN")
        --pawnCount
    }

    let pieces = []
    let position = new Position(0, 0)

    for (let i = 0; i < queenCount; ++i)
      pieces.push(new Piece(position, color, "QUEEN"))

    for (let i = 0; i < rookCount; ++i)
      pieces.push(new Piece(position, color, "ROOK"))

    for (let i = 0; i < bishopCount; ++i)
      pieces.push(new Piece(position, color, "BISHOP"))

    for (let i = 0; i < knightCount; ++i)
      pieces.push(new Piece(position, color, "KNIGHT"))

    for (let i = 0; i < pawnCount; ++i)
      pieces.push(new Piece(position, color, "PAWN"))

    return pieces
  }

  getPieceImage(piece: Piece): string {
    return getPieceImage(piece)
  }

  cancelTheGame(): void {
    localStorage.clear();
    window.location.reload();
  }
}
