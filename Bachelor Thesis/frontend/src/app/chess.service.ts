import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Board} from "./models/board";
import {Move} from "./models/move";
import {ExecuteMove} from "./models/execute-move";

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

  getStart(): Observable<Board> {
    return this.http.get<Board>(`${this.backendURL}/start`)
  }

  getAvailableMoves(board: Board): Observable<Array<Move>> {
    return this.http.post<Array<Move>>(`${this.backendURL}/getmoves`, board)
  }

  move(executeMove: ExecuteMove): Observable<Board> {
    return this.http.post<Board>(`${this.backendURL}/move`, executeMove)
  }

  computeMove(board: Board): Observable<Board> {
    return this.http.post<Board>(`${this.backendURL}/compute`, board)
  }
}
