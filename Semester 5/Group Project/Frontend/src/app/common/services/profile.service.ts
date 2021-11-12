import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

import { User } from "../models/user.model";

@Injectable()
export class ProfileService {
  private baseUrl = "http://localhost:8080/api/account/";

  constructor(private httpClient: HttpClient) {
  }

  getUserByUsername(username: string): Observable<User> {
    return this.httpClient.get<User>(this.baseUrl + `getUserByUsername/${username}`);
  }

  updateProfile(username: string, password: string, user: User): Observable<any> {
    return this.httpClient.put(this.baseUrl + `change/${username}/${password}`, user);
  }
}
