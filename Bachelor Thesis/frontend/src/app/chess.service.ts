import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";

import { Board } from "./models/board";
import { Move } from "./models/move";
import { ExecuteMove } from "./models/execute-move";
import {Options} from "./models/options";

@Injectable({
  providedIn: 'root'
})
export class ChessService {
  private backendURL = "http://localhost:8080"

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  newGame(options: Options): Observable<string> {
    return this.http.post<string>(`${this.backendURL}/new-game`, options)
  }

  getGame(id: string): Observable<Board> {
    return this.http.get<Board>(`${this.backendURL}/get-game/${id}`)
  }

  computeMove(id: string): Observable<Board> {
    return this.http.post<Board>(`${this.backendURL}/compute/${id}`, {})
  }

  move(id: string, executeMove: ExecuteMove): Observable<Board> {
    return this.http.post<Board>(`${this.backendURL}/move/${id}`, executeMove)
  }

  getMoves(id: string): Observable<Array<Move>> {
    return this.http.get<Array<Move>>(`${this.backendURL}/get-moves/${id}`)
  }
}
