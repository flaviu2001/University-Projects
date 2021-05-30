import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CustomersService} from "../customers-service";

@Component({
  selector: 'app-customer-update',
  templateUrl: './customer-update.component.html',
  styleUrls: ['./customer-update.component.css']
})
export class CustomerUpdateComponent implements OnInit {

  name = ""
  phoneNumber = ""
  size = ""
  color = ""

  constructor(private service: CustomersService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.service.getCustomer(this.route.snapshot.queryParams.id).subscribe(customer => {
      this.name = customer.name
      this.phoneNumber = customer.phoneNumber
      if (customer.petHouseDTO != null) {
        this.size = ""+customer.petHouseDTO.size
        this.color = customer.petHouseDTO.color
      }
    })
  }

  updateCustomer(data): void {
    if (!new RegExp("^[0-9]{10}$").test(data.phoneNumber)) {
      alert("Phone number invalid")
      return
    }
    const id = this.route.snapshot.queryParams.id;
    this.service.updateCustomer(id, data.name, data.phoneNumber, data.size, data.color).subscribe(() => {
      this.router.navigate(['showCustomers']).then(_ => {
      });
    });
  }

  onCancel(): void {
    this.router.navigate(['showCustomers']).then(_ => {
    });
  }

}