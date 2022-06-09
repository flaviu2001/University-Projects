import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import {Component, Inject, OnInit} from '@angular/core';

@Component({
  selector: 'app-promotion-choice',
  templateUrl: './promotion-choice.component.html',
  styleUrls: ['./promotion-choice.component.sass']
})
export class PromotionChoiceComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<PromotionChoiceComponent>, @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void {
  }

  setChoice(choice: number): void {
    this.data.choice = choice
    this.dialogRef.close(this.data.choice);
  }

  getImage(choice: number): string {
    let color = ""
    if (this.data.color == "WHITE")
      color = "w"
    else color = "b"
    let piece = ""
    if (choice == 1)
      piece = "N"
    else if (choice == 2)
      piece = "B"
    else if (choice == 3)
      piece = "R"
    else piece = "Q"

    return `assets/cburnett/${color}${piece}.svg`
  }
}
