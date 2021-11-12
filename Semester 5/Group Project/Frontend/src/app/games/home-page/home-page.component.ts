import { Component, OnInit } from '@angular/core';

import { Game } from "../../common/models/game.model";
import { GameService } from "../../common/services/game.service";

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {
  games: Game[] = [];

  constructor(private gameService: GameService) {
  }

  ngOnInit(): void {
    this.gameService.getAllGames().subscribe(
      (games) => {
        this.games = games;
      });

  }
}
