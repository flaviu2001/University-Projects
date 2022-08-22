import { Component, OnInit } from '@angular/core';

import { MatDialog, MatDialogRef } from "@angular/material/dialog";

import { SetupComponent } from "../setup/setup.component";

@Component({
  selector: 'app-choose-color-to-play',
  templateUrl: './choose-color-to-play.component.html',
  styleUrls: ['./choose-color-to-play.component.sass']
})
export class ChooseColorToPlayComponent implements OnInit {

  constructor(private dialog: MatDialog,
              private dialogRef: MatDialogRef<ChooseColorToPlayComponent>) { }

  ngOnInit(): void {
  }

  public play(color: string): void {

    this.dialog.open(SetupComponent, {
      disableClose: true,
      width: '450px',
      data: { color: color }
    });

    this.dialogRef.close();

  }

}
