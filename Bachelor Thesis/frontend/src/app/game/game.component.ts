import { Component, OnInit } from '@angular/core';

import { MatDialog } from "@angular/material/dialog";

import { ChooseColorToPlayComponent } from "../choose-color-to-play/choose-color-to-play.component";

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.sass']
})
export class GameComponent implements OnInit {

  gameID: string | null = null
  playerColor: string = ""

  constructor(private dialog: MatDialog) { }

  ngOnInit(): void {
    this.gameID = localStorage.getItem('gameID');
    // @ts-ignore
    this.playerColor = localStorage.getItem('playerColor');

    if(!this.gameID) {
      this.dialog.open(ChooseColorToPlayComponent, {
        disableClose: true,
        width: '500px'
      });
    }
  }

}
