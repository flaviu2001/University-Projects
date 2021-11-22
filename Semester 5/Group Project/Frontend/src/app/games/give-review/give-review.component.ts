import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";

import { ReviewService } from "../../common/services/review.service";
import { ProfileService } from "../../common/services/profile.service";
import { User } from "../../common/models/user.model";
import { Game } from "../../common/models/game.model";
import { Review } from "../../common/models/review.model";

export interface DialogData {
  numberOfStars: number;
  game: Game;
}

@Component({
  selector: 'app-give-review',
  templateUrl: './give-review.component.html',
  styleUrls: ['./give-review.component.css']
})
export class GiveReviewComponent implements OnInit {
  username: string = '';
  user: User = {} as User;

  constructor(public dialogRef: MatDialogRef<GiveReviewComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData,
              private reviewService: ReviewService,
              private profileService: ProfileService) { }

  ngOnInit(): void {
    this.username = String(localStorage.getItem('username'));
    this.profileService.getUserByUsername(this.username).subscribe((user) => {
      this.user = user;
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  sendReview(text: string) {
    let review = new Review(this.data.numberOfStars, text, this.user, this.data.game);
    this.reviewService.addReview(review).subscribe(() => {
      window.location.reload();
    });
    this.dialogRef.close();
  }
}
