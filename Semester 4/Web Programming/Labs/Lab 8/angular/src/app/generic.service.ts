import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import {Observable, of} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {User} from './user';

@Injectable({
  providedIn: 'root'
})
export class GenericService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };
  private backendUrl = 'http://localhost/lab7/';

  constructor(private http: HttpClient) {
  }

  fetchUsers(role: string, name: string): Observable<User[]> {
    /* body of the method */
    return this.http.get<User[]>(this.backendUrl + `showUsers.php?role=${role}&name=${name}`)
      .pipe(catchError(this.handleError<User[]>('fetchStudents', []))
      );
  }

  deleteUser(userId: number): Observable<any> {
    return this.http.post(this.backendUrl + `deleteUserBackend.php`, {id: userId});
  }

  addUser(nameOf: string, usernameOf: string, passwordOf: string,
          ageOf: number, roleOf: string, emailOf: string, webpageOf: string): Observable<any> {
    return this.http.post(this.backendUrl + `addUser.php`, {
      name: nameOf,
      username: usernameOf,
      password: passwordOf,
      age: ageOf,
      role: roleOf,
      email: emailOf,
      webpage: webpageOf
    });
  }

  updateUser(idOf: number, nameOf: string, usernameOf: string, passwordOf: string,
             ageOf: number, roleOf: string, emailOf: string, webpageOf: string): Observable<any> {
    return this.http.post(this.backendUrl + `updateUserBackend.php`, {
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

  private handleError<T>(operation = 'operation', result?: T): (error: any) => Observable<T> {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }
}
