import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Injectable } from "@angular/core";

import { User } from "../models/user.model";

@Injectable()
export class LoginRegisterService {
  private baseUrl = "http://localhost:8080/api/account/";

  constructor(private httpClient: HttpClient) {

  }

  login(username: string, password: string): Observable<User> {
    return this.httpClient.get<User>(this.baseUrl + `login/${username}/${password}`);
  }

  register(user: User): Observable<any> {
    return this.httpClient.post(this.baseUrl + `register`, user);
  }

  verifyEmail(username: string, code: number): Observable<boolean> {
    return this.httpClient.get<boolean>(this.baseUrl + `verification/${username}/${code}`);
  }

}
