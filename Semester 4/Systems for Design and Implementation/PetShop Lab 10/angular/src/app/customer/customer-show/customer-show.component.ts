import { Component, OnInit } from '@angular/core';
import {Cat} from "../../cats/cats-model";
import {CatsService} from "../../cats/cats-service";
import {Router} from "@angular/router";
import {Customer} from "../customers-model";
import {CustomersService} from "../customers-service";

@Component({
  selector: 'app-customer-show',
  templateUrl: './customer-show.component.html',
  styleUrls: ['./customer-show.component.css']
})
export class CustomerShowComponent implements OnInit {

  customers: Array<Customer>;

  constructor(private service: CustomersService, private router: Router) {
  }

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.service.getCustomers().subscribe(customers => this.customers = customers.customers);
  }

  navigateToDelete(customerId: number): void {
    this.router.navigate(['deleteCustomer'], {queryParams: {id: customerId}}).then(_ => {
    });
  }

  navigateToAdd(): void {
    this.router.navigate(['addCustomer']).then(_ => {
    });
  }

  navigateToUpdate(catId: number): void {
    this.router.navigate(['updateCustomer'], {queryParams: {id: catId}}).then(_ => {
    });
  }

  onBack() {
    this.router.navigate(['switcher']).then(_ => {
    })
  }
}
