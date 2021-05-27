import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {Food} from './foods-model';

@Injectable({
  providedIn: 'root'
})
export class FoodsService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };
  private backendUrl = 'http://localhost:8080/api/foods';

  constructor(private http: HttpClient) {
  }

  getFoods(): Observable<Array<Food>> {
    return this.http.get<Array<Food>>(this.backendUrl);
  }

  getFood(foodId: number): Observable<Food> {
    return this.getFoods().pipe(
      map(foods => foods.find(food => food.id == foodId))
    );
  }

  addFood(name: string, producer: string, expirationDate: string): Observable<any> {
    return this.http.post(this.backendUrl, new Food(name, producer, expirationDate));
  }

  deleteFood(foodId: number): Observable<any> {
    return this.http.delete(this.backendUrl + `/${foodId}`);
  }

  updateFood(foodId: number, newName: string, newProducer: string, newExpirationDate: string): Observable<any> {
    return this.http.put(this.backendUrl, {
      id: foodId,
      name: newName,
      producer: newProducer,
      expirationDate: newExpirationDate
    });
  }
}
