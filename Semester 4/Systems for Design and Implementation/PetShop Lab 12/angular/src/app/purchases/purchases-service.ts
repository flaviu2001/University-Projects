import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Purchase, PurchaseAddDTO, PurchasesDTO} from "./purchases-model";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class PurchasesService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };
  private backendUrl = 'http://localhost:8080/api/purchases';
  private catBackendUrl = 'http://localhost:8080/api/purchases-of-cat';
  private customerBackendUrl = 'http://localhost:8080/api/purchases-of-customer';

  constructor(private http: HttpClient) {
  }

  getPurchases(catId, customerId): Observable<Array<Purchase>> {
    if (catId != null)
      return this.http.get<PurchasesDTO>(this.catBackendUrl + `/${catId}`).pipe(map(purchases => purchases.purchases))
    return this.http.get<PurchasesDTO>(this.customerBackendUrl + `/${customerId}`).pipe(map(purchases => purchases.purchases))
  }

  addPurchase(price: number,
              dateAcquired: string,
              review: number,
              catId: number,
              customerId: number,): Observable<any> {
    return this.http.post(this.backendUrl, new PurchaseAddDTO(price, dateAcquired, review, catId, customerId));
  }
}
