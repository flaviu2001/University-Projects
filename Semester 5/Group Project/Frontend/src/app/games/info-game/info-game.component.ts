import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";

import { GameService } from "../../common/services/game.service";
import { Game } from "../../common/models/game.model";
import { GiveReviewComponent } from "../give-review/give-review.component";
import  {Review } from "../../common/models/review.model";
import {ReviewService} from "../../common/services/review.service";

@Component({
  selector: 'app-info-game',
  templateUrl: './info-game.component.html',
  styleUrls: ['./info-game.component.css']
})
export class InfoGameComponent implements OnInit {
  title: string = '';
  game: Game = {} as Game;
  reviews: Review[] = [];

  stars: number[] = [1, 2, 3, 4, 5];
  selectedValue: number = -1;

  constructor(private gameService: GameService, private reviewService: ReviewService,
              private route: ActivatedRoute, private router: Router,
              private dialog: MatDialog) { }

  ngOnInit(): void {
    this.title = this.route.snapshot.queryParams.title;

    this.gameService.getGameByTitle(this.title).subscribe((game) => {
      this.game = game;
    });

    this.reviewService.getReviewsOfGame(this.title).subscribe((reviews) => {
      this.reviews = reviews;
      });
  }

  openDialog(numberOfStars: number) {
    this.dialog.open(GiveReviewComponent, {
      width: '300px',
      data: {numberOfStars: numberOfStars, game: this.game}
    });
  }

  countStar(star: number) {
    this.selectedValue = star;
    this.openDialog(this.selectedValue);
  }

  goToPurchaseGamePage(title: string) {
    this.router.navigate(['purchaseGame'], {
      queryParams: {
        title: title
      }
    }).then(_ => {});
  }
}
