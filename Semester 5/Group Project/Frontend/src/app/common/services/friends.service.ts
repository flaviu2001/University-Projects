import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

import { FriendInvitation } from "../models/friend-invitation.model";

@Injectable()
export class FriendsService {
  private baseUrl = "http://localhost:8080/api/account/friends/";

  constructor(private httpClient: HttpClient) {
  }

  getAllFriends(username: string): Observable<FriendInvitation[]> {
    return this.httpClient.get<FriendInvitation[]>(this.baseUrl+"get/"+username);
  }

  getAll(username: string): Observable<FriendInvitation[]> {
    return this.httpClient.get<FriendInvitation[]>(this.baseUrl+"getAll/"+username);
  }

  accept(friendInvitation: FriendInvitation): Observable<any> {
    return this.httpClient.post<any>(this.baseUrl+"accept", friendInvitation);
  }

  reject(friendInvitation: FriendInvitation): Observable<any> {
    return this.httpClient.post<any>(this.baseUrl+"reject", friendInvitation);
  }

  add(friendInvitation: FriendInvitation): Observable<any> {
    return this.httpClient.post<any>(this.baseUrl+"add", friendInvitation);
  }
}
