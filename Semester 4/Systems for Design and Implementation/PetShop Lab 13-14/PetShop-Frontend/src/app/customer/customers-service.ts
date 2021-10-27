import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {Customer} from './customers-model';

@Injectable({
  providedIn: 'root'
})
export class CustomersService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };
  private backendUrl = 'http://localhost:8080/api/customers';

  constructor(private http: HttpClient) {
  }

  getCustomers(): Observable<Array<Customer>> {
    return this.http.get<Array<Customer>>(this.backendUrl);
  }

  getCustomer(customerId: number): Observable<Customer> {
    return this.getCustomers().pipe(
      map(customers => customers.find(customer => customer.id == customerId))
    );
  }

  addCustomer(name: string, phoneNumber: string): Observable<any> {
    return this.http.post(this.backendUrl, new Customer(name, phoneNumber));
  }

  deleteCustomer(customerId: number): Observable<any> {
    return this.http.delete(this.backendUrl + `/${customerId}`);
  }

  updateCustomer(customerId: number, newName: string, newPhoneNumber: string): Observable<any> {
    return this.http.put(this.backendUrl, {
      id: customerId,
      name: newName,
      phoneNumber: newPhoneNumber
    });
  }
}
