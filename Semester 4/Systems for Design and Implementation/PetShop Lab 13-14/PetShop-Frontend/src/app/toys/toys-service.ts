import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {Toy, ToysDTO} from './toys-model';
import {Cat} from '../cats/cats-model';

@Injectable({
  providedIn: 'root'
})
export class ToysService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };
  private backendUrl = 'http://localhost:8080/api/toys';

  constructor(private http: HttpClient) {
  }

  getToysOfCat(catId: number): Observable<ToysDTO> {
    return this.http.get<ToysDTO>(this.backendUrl + `/${catId}`);
  }

  getToy(toyId: number): Observable<Toy> {
    return this.http.get<ToysDTO>(this.backendUrl).pipe(map(toys => toys.toys.find(toy => toy.id == toyId)));
  }

  addToy(name: string, price: number, catId: number): Observable<any> {
    return this.http.post(this.backendUrl, new Toy(name, price, catId));
  }

  deleteToy(toyId: number): Observable<any> {
    return this.http.delete(this.backendUrl + `/${toyId}`);
  }

  updateToy(toyId: number, newName: string, newPrice: number, newCat: Cat): Observable<any> {
    return this.http.put(this.backendUrl + `/${toyId}`, {
      name: newName,
      price: newPrice,
      cat: newCat
    });
  }
}
