import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';
import {User} from './user';

@Injectable({
  providedIn: 'root'
})
export class Service {
  private backendUrl = 'http://localhost:5000/Home/';

  constructor(private http: HttpClient) {
  }

  getUsers(role: string, name: string): Observable<User[]> {
    return this.http.get<User[]>(this.backendUrl + `GetUsers?role=${role}&name=${name}`);
  }

  getUser(id: number): Observable<User> {
    return this.http.get<User>(this.backendUrl + `GetUser?id=${id}`);
  }

  deleteUser(userId: number): Observable<any> {
    return this.http.delete(this.backendUrl + `DeleteUser/` + userId);
  }

  addUser(nameOf: string, usernameOf: string, passwordOf: string,
          ageOf: number, roleOf: string, emailOf: string, webpageOf: string): Observable<any> {
    return this.http.post(this.backendUrl + `AddUser`, new User(0, nameOf, usernameOf, passwordOf, ageOf, roleOf, emailOf, webpageOf));
  }

  updateUser(idOf: number, nameOf: string, usernameOf: string, passwordOf: string,
             ageOf: number, roleOf: string, emailOf: string, webpageOf: string): Observable<any> {
    return this.http.put(this.backendUrl + `UpdateUser`, {
      id: idOf,
      name: nameOf,
      password: passwordOf,
      username: usernameOf,
      age: ageOf,
      role: roleOf,
      email: emailOf,
      webpage: webpageOf
    });
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post(this.backendUrl + `LoginUser`, {
      id: 0,
      name: "",
      username: username,
      password: password,
      age: 0,
      role: "",
      email: "",
      webpage: "",
    })
  }
}
