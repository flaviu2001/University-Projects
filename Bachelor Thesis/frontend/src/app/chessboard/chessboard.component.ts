import {Component, OnInit} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import {Position} from "../models/position";
import {Board} from "../models/board";
import {ChessService} from "../chess.service";
import {Piece} from "../models/piece";
import {Move} from "../models/move";
import {ExecuteMove} from "../models/execute-move";
import {PromotionChoiceComponent} from "./promotion-choice/promotion-choice.component";
import {Observable} from "rxjs";

@Component({
  selector: 'app-chessboard',
  templateUrl: './chessboard.component.html',
  styleUrls: ['./chessboard.component.sass']
})
export class ChessboardComponent implements OnInit {
  positions: Array<Position> = []
  board: Board | null = null
  activePosition: Position | null = null
  possibleMoves: Array<Move> = []
  canClick: Array<Position> = []
  playerColor: string = "WHITE"
  computerVsPlayer: boolean = true

  constructor(private chessService: ChessService, private dialog: MatDialog) { }

  ngOnInit(): void {
    if (this.playerColor == "WHITE")
      this.positions = Array.from({length: 64}, (_, i) => new Position(7-Math.floor(i/8), i%8))
    else
      this.positions = Array.from({length: 64}, (_, i) => new Position(Math.floor(i/8), i%8))
    if (this.playerColor == "WHITE" || !this.computerVsPlayer)
      this.chessService.getStart().subscribe(board => {
        this.board = board
        this.refreshPossibleMoves()
      })
    else
      this.chessService.getStart().subscribe(board => {
        this.board = board
        this.refreshPossibleMoves()
        if (this.computerVsPlayer)
          this.chessService.computeMove(this.board).subscribe(newerBoard => {
            this.board = newerBoard
            this.refreshPossibleMoves()
          })
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
      this.chessService.getAvailableMoves(this.board).subscribe(moves => this.possibleMoves = moves)
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

  executeMove(move: Move, choice: number): void {
    this.chessService.move(new ExecuteMove(this.board!!, move, choice)).subscribe(newBoard => {
      this.board = newBoard
      this.refreshPossibleMoves()
      if (this.computerVsPlayer && this.board.state == "UNFINISHED")
        this.chessService.computeMove(this.board).subscribe(newerBoard => {
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
}
