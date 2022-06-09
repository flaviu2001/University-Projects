import {Piece} from "./piece";
import {HistoryMove} from "./history-move";

export class Board {
  pieceList: Array<Piece>
  currentColor: string
  historyOfMoves: Array<HistoryMove>
  state: string

  constructor(pieceList: Array<Piece>, currentColor: string, historyOfMoves: Array<HistoryMove>, state: string) {
    this.pieceList = pieceList;
    this.currentColor = currentColor;
    this.historyOfMoves = historyOfMoves;
    this.state = state;
  }
}
