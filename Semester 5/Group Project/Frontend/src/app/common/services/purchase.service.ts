import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

import { Purchase } from "../models/purchase.model";

@Injectable()
export class PurchaseService {
  private baseUrl = "http://localhost:8080/api/purchase/";

  constructor(private httpClient: HttpClient) {
  }

  addPurchase(purchase: Purchase): Observable<any> {
    return this.httpClient.post<any>(this.baseUrl, purchase);
  }
}
