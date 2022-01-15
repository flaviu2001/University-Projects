import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";

import { Review } from "../models/review.model";
import { Observable } from "rxjs";

@Injectable()
export class ReviewService {
  private baseUrl = "http://localhost:8080/api/review/";

  constructor(private httpClient: HttpClient) {
  }

  addReview(review: Review): Observable<any> {
    return this.httpClient.post(this.baseUrl, review);
  }

  getReviewsOfGame(title: string): Observable<Review[]> {
    return this.httpClient.get<Review[]>(this.baseUrl + `getReviewsOfGame/${title}`);
  }

  getReviewsOfUser(username: string): Observable<Review[]> {
    return this.httpClient.get<Review[]>(this.baseUrl + `getReviewsOfUser/${username}`);
  }
}
