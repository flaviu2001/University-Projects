import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {CustomersService} from '../customers-service';

@Component({
  selector: 'app-customer-add',
  templateUrl: './customer-add.component.html',
  styleUrls: ['./customer-add.component.css']
})
export class CustomerAddComponent implements OnInit {

  constructor(private service: CustomersService, private router: Router) {
  }

  ngOnInit(): void {
  }

  addCustomer(data): void {
    if (!new RegExp('^[0-9]{10}$').test(data.phoneNumber)) {
      alert('Phone number invalid');
      return;
    }
    this.service.addCustomer(data.name, data.phoneNumber).subscribe(() => {
      this.router.navigate(['showCustomers']).then(_ => {
      });
    });
  }

  onCancel(): void {
    this.router.navigate(['showCustomers']).then(_ => {
    });
  }
}
