import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

import { Game } from "../models/game.model";

@Injectable()
export class GameService {
  private baseUrl = "http://localhost:8080/api/game/";

  constructor(private httpClient: HttpClient) {
  }

  getAllGames(): Observable<Game[]> {
    return this.httpClient.get<Game[]>(this.baseUrl);
  }

  getGamesFromUser(name: String): Observable<Game[]> {
    return this.httpClient.get<Game[]>(`${this.baseUrl}fromUser/${name}`);
  }

  getWishedGamesForUser(username: string) {
    return this.httpClient.get<Game[]>(this.baseUrl + `getWishList/${username}`);
  }

  getGameByTitle(title: string): Observable<Game> {
    return this.httpClient.get<Game>(this.baseUrl + `getGameByTitle/${title}`);
  }

  searchGames(game: string): Observable<Game[]> {
    return this.httpClient.get<Game[]>(this.baseUrl + `searchGames/${game}`);
  }
}
