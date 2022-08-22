import { Component, Inject, OnInit } from '@angular/core';

import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";

import { ChessService } from "../chess.service";
import {Options} from "../models/options";

@Component({
  selector: 'app-setup',
  templateUrl: './setup.component.html',
  styleUrls: ['./setup.component.sass']
})
export class SetupComponent implements OnInit {

  enableAlphaBetaPruning: true | false = true;
  enableQuiescence: true | false = false;
  enableTranspositionTable: true | false = true;
  enableParallelization: true | false = false;
  iterativeDeepeningLowCutoff: string = '500';
  iterativeDeepeningHighCutoff: string = '1500';

  constructor(private dialogRef: MatDialogRef<SetupComponent>,
              @Inject(MAT_DIALOG_DATA) private color: string,
              private service: ChessService) { }

  ngOnInit(): void {
    // @ts-ignore
    this.color = this.color.color;
  }

  public setupFinished(): void {
    const options = new Options(this.enableAlphaBetaPruning, this.enableQuiescence, this.enableTranspositionTable, this.enableParallelization, +this.iterativeDeepeningLowCutoff, +this.iterativeDeepeningHighCutoff)

    this.service.newGame(options).subscribe(id => {
      localStorage.setItem('playerColor', this.color);
      localStorage.setItem('gameID', id);
      this.dialogRef.close();
      window.location.reload();
    });
  }

}
