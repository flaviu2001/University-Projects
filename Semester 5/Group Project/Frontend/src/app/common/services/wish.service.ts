import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";

import { Wish } from "../models/wish.model";
import { Game } from "../models/game.model";

@Injectable({
  providedIn: 'root'
})
export class WishService {
  private baseUrl = "http://localhost:8080/api/wish/";
  constructor(private httpClient: HttpClient) { }

  addWish(wish: Wish) {
    return this.httpClient.post<Wish>(this.baseUrl + `add`, wish);
  }

  removeWish(game: Game, username: string) {
    return this.httpClient.delete<null>(this.baseUrl + `delete/${game.id}/${username}`)
  }
}
