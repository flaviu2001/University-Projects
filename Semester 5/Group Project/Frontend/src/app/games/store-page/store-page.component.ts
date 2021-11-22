import { Component, OnInit } from '@angular/core';
import { PageEvent } from "@angular/material/paginator";
import { Router } from "@angular/router";

import { Game } from "../../common/models/game.model";
import { GameService } from "../../common/services/game.service";

@Component({
  selector: 'app-store-page',
  templateUrl: './store-page.component.html',
  styleUrls: ['./store-page.component.css']
})
export class StorePageComponent implements OnInit {
  pageEvent: PageEvent | undefined;

  games: Game[] = [];
  pagedList: Game[] = [];

  constructor(private gameService: GameService, private router: Router) {
  }

  ngOnInit(): void {
    this.gameService.getAllGames().subscribe(
      (games) => {
        this.games = games;
        this.pagedList = this.games.slice(0, 3);
      });
  }

  onPaginateChange(event: PageEvent) {
    let startIndex = event.pageIndex * event.pageSize;
    let endIndex = startIndex + event.pageSize;

    if(endIndex > this.games.length){
      endIndex = this.games.length;
    }

    this.pagedList = this.games.slice(startIndex, endIndex);
  }

  goToInfoGamePage(title: string) {
    this.router.navigate(['infoGame'], {
      queryParams: {
        title: title
      }
    }).then(_ => {});
  }

  goToPurchaseGamePage(title: string) {
    this.router.navigate(['purchaseGame'], {
      queryParams: {
        title: title
      }
    }).then(_ => {});
  }
}
