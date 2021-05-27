import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Cat, CatsDTO} from './cats-model';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CatsService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };
  private backendUrl = 'http://localhost:8080/api/cats';

  constructor(private http: HttpClient) {
  }

  getCats(): Observable<CatsDTO> {
    return this.http.get<CatsDTO>(this.backendUrl);
  }

  getCatsByBreed(breed: string): Observable<CatsDTO> {
    return this.http.get<CatsDTO>(this.backendUrl + `/${breed}`, {});
  }

  getCat(catId: number): Observable<Cat> {
    return this.getCats().pipe(
      map(cats => cats.cats.find(cat => cat.id == catId))
    );
  }

  addCat(name: string, breed: string, catYears: number): Observable<any> {
    return this.http.post(this.backendUrl, new Cat(name, breed, catYears));
  }

  deleteCat(catId: number): Observable<any> {
    return this.http.delete(this.backendUrl + `/${catId}`);
  }

  updateCat(catId: number, newName: string, newBreed: string, newCatYears: number): Observable<any> {
    return this.http.put(this.backendUrl + `/${catId}`, {
      name: newName,
      breed: newBreed,
      catYears: newCatYears
    });
  }
}
