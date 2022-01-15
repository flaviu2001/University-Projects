import { Component, OnInit } from '@angular/core';
import { PageEvent } from "@angular/material/paginator";
import { Router } from "@angular/router";

import { Game } from "../../common/models/game.model";
import { GameService } from "../../common/services/game.service";
import { WishService } from "../../common/services/wish.service";

@Component({
  selector: 'app-wish-list',
  templateUrl: './wish-list.component.html',
  styleUrls: ['./wish-list.component.css']
})
export class WishListComponent implements OnInit {
  pageEvent: PageEvent | undefined;

  pagedList: Game[] = [];
  wishListGames: Game[] = []

  constructor(private gameService: GameService, private wishService: WishService, private router: Router) {
  }

  ngOnInit(): void {
    this.gameService.getWishedGamesForUser(localStorage.getItem('username')!!).subscribe(games => {
      this.wishListGames = games;
      this.pagedList = this.wishListGames.slice(0, 3);
    })
  }

  onPaginateChange(event: PageEvent) {
    let startIndex = event.pageIndex * event.pageSize;
    let endIndex = startIndex + event.pageSize;

    if(endIndex > this.wishListGames.length){
      endIndex = this.wishListGames.length;
    }

    this.pagedList = this.wishListGames.slice(startIndex, endIndex);
  }

  gameWished(game: Game) {
    for (let ownedGame of this.wishListGames)
      if (ownedGame.id == game.id)
        return true;
    return false;
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
}
