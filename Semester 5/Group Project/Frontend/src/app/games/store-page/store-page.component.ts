import { Component, OnInit } from '@angular/core';
import { PageEvent } from "@angular/material/paginator";
import { Router } from "@angular/router";

import { Game } from "../../common/models/game.model";
import { GameService } from "../../common/services/game.service";
import { WishService } from "../../common/services/wish.service";
import { Wish } from "../../common/models/wish.model";

@Component({
  selector: 'app-store-page',
  templateUrl: './store-page.component.html',
  styleUrls: ['./store-page.component.css']
})
export class StorePageComponent implements OnInit {
  pageEvent: PageEvent | undefined;

  games: Game[] = [];
  pagedList: Game[] = [];
  ownedGames: Game[] = [];
  wishListGames: Game[] = []

  constructor(private gameService: GameService, private wishService: WishService, private router: Router) {
  }

  ngOnInit(): void {
    this.gameService.getAllGames().subscribe(
      (games) => {
        this.games = games;
        this.pagedList = this.games.slice(0, 3);
      });
    this.gameService.getGamesFromUser(localStorage.getItem('username')!!).subscribe(games => {
      this.ownedGames = games;
    })
    this.gameService.getWishedGamesForUser(localStorage.getItem('username')!!).subscribe(games => {
      this.wishListGames = games;
    })
  }

  searchGames(game: string) {
    this.gameService.searchGames(game).subscribe(
      (games) => {
        this.games = games;
        this.pagedList = this.games.slice(0, 3);
      }
    );
  }

  gameOwned(game: Game): boolean {
    for (let ownedGame of this.ownedGames)
      if (ownedGame.id == game.id)
        return true;
    return false;
  }

  onPaginateChange(event: PageEvent) {
    let startIndex = event.pageIndex * event.pageSize;
    let endIndex = startIndex + event.pageSize;

    if(endIndex > this.games.length){
      endIndex = this.games.length;
    }

    this.pagedList = this.games.slice(startIndex, endIndex);
  }

  gameWished(game: Game) {
    for (let ownedGame of this.wishListGames)
      if (ownedGame.id == game.id)
        return true;
    return false;
  }


  addToWishList(game: Game) {
    let wish: Wish = {id: -1, gameID: game.id, arcaneUserName: localStorage.getItem('username')!!}
    this.wishService.addWish(wish).subscribe((w) => {
      window.location.reload()
    })
  }

  removeFromWishList(game: Game) {
    this.wishService.removeWish(game, localStorage.getItem('username')!!).subscribe(() => {
      window.location.reload()
    })
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
