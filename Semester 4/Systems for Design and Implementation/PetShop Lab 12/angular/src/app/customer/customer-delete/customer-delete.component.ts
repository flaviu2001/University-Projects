import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CustomersService} from "../customers-service";

@Component({
  selector: 'app-customer-delete',
  templateUrl: './customer-delete.component.html',
  styleUrls: ['./customer-delete.component.css']
})
export class CustomerDeleteComponent implements OnInit {

  constructor(private service: CustomersService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
  }

  onYes(): void {
    this.service.deleteCustomer(this.route.snapshot.queryParams.id).subscribe(() => {
      this.router.navigate(['showCustomers']).then(_ => {
      });
    });
  }

  onNo(): void {
    this.router.navigate(['showCustomers']).then(_ => {
    });
  }

}
