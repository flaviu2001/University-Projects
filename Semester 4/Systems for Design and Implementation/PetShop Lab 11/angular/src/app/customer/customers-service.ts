import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {Customer, CustomersDTO} from "./customers-model";

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

  getCustomers(): Observable<CustomersDTO> {
    return this.http.get<CustomersDTO>(this.backendUrl);
  }

  getCustomer(customerId: number): Observable<Customer> {
    return this.getCustomers().pipe(
      map(customers => customers.customers.find(customer => customer.id == customerId))
    );
  }

  addCustomer(name: string, phoneNumber: string, color: string, size: string): Observable<any> {
    let petHouse = null
    if (color && size) {
      petHouse = {
        color: color,
        size: size
      }
    }
    console.log(new Customer(name, phoneNumber, petHouse))
    return this.http.post(this.backendUrl, new Customer(name, phoneNumber, petHouse));
  }

  deleteCustomer(customerId: number): Observable<any> {
    return this.http.delete(this.backendUrl + `/${customerId}`);
  }

  updateCustomer(customerId: number, newName: string, newPhoneNumber: string, size: string, color: string): Observable<any> {
    let petHouse = null
    if (size && color)
      petHouse = {
        color: color,
        size: size
      }
    return this.http.put(this.backendUrl + `/${customerId}`, {
      name: newName,
      phoneNumber: newPhoneNumber,
      petHouseDTO: petHouse
    });
  }
}
