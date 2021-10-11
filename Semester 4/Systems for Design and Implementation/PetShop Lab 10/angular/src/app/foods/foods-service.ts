import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Cat, CatsDTO} from "../cats/cats-model";
import {map} from "rxjs/operators";
import {Food, FoodsDTO} from "./foods-model";

@Injectable({
  providedIn: 'root'
})
export class FoodsService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };
  private backendUrl = 'http://localhost:8080/api/food';

  constructor(private http: HttpClient) {
  }

  getFoods(): Observable<FoodsDTO> {
    return this.http.get<FoodsDTO>(this.backendUrl);
  }

  getFood(foodId: number): Observable<Food> {
    return this.getFoods().pipe(
      map(foods => foods.foods.find(food => food.id == foodId))
    );
  }

  addFood(name: string, producer: string, expirationDate: string): Observable<any> {
    return this.http.post(this.backendUrl, new Food(name, producer, expirationDate));
  }

  deleteFood(foodId: number): Observable<any> {
    return this.http.delete(this.backendUrl + `/${foodId}`);
  }

  updateFood(foodId: number, newName: string, newProducer: string, newExpirationDate: string): Observable<any> {
    return this.http.put(this.backendUrl + `/${foodId}`, {
      name: newName,
      producer: newProducer,
      expirationDate: newExpirationDate
    });
  }
}
