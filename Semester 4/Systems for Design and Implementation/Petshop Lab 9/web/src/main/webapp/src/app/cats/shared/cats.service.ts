import {Injectable} from '@angular/core';

import {HttpClient} from "@angular/common/http";

import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {Cat, CatsDTO} from "./cats.model";


@Injectable()
export class CatService {
  private catsUrl = 'http://localhost:8080/api/cats';

  constructor(private httpClient: HttpClient) {
  }

  getCats(): Observable<CatsDTO> {
    return this.httpClient
      .get<CatsDTO>(this.catsUrl);
  }

  getCat(id: number): Observable<Cat> {
    return this.getCats()
      .pipe(
        map(students => students.cats.find(student => student.id === id))
      );
  }

  update(cat): Observable<Cat> {
    const url = `${this.catsUrl}/${cat.id}`;
    return this.httpClient
      .put<Cat>(url, cat);
  }
}
