import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import * as moment from 'moment';
import {Purchase} from '../purchases-model';
import {PurchasesService} from '../purchases-service';
import {Cat} from '../../cats/cats-model';
import {Customer} from '../../customer/customers-model';
import {CatsService} from '../../cats/cats-service';
import {CustomersService} from '../../customer/customers-service';

@Component({
  selector: 'app-purchases-show',
  templateUrl: './purchases-show.component.html',
  styleUrls: ['./purchases-show.component.css']
})
export class PurchasesShowComponent implements OnInit {
  purchases: Array<Purchase>;
  cats: Array<Cat> = null;
  customers: Array<Customer> = null;
  catId;
  customerId;

  constructor(private service: PurchasesService, private catService: CatsService,
              private customerService: CustomersService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.catService.getCats().subscribe(cats => this.cats = cats.cats);
    this.customerService.getCustomers().subscribe(customers => this.customers = customers);
    this.refresh();
  }

  refresh(): void {
    this.catId = this.route.snapshot.queryParams.catId;
    this.customerId = this.route.snapshot.queryParams.customerId;
    if (typeof this.catId === 'undefined') {
      this.catId = null;
    }
    if (typeof this.customerId === 'undefined') {
      this.customerId = null;
    }
    if (this.cats == null || this.customers == null){
      setTimeout(() => {
        this.refresh();
      }, 100);
      return;
    }
    this.service.getPurchases(this.catId, this.customerId).subscribe(purchases => {
      this.purchases = purchases;
      for (const purchase of this.purchases) {
        purchase.cat = this.cats.find(cat => cat.id == purchase.id.catId);
        purchase.customer = this.customers.find(customer => customer.id == purchase.id.customerId);
      }
    });
  }

  navigateToAdd(): void {
    if (this.catId != null) {
      this.router.navigate(['addPurchase'], {queryParams: {catId: this.catId}}).then(_ => {
      });
    }
    else { this.router.navigate(['addPurchase'], {queryParams: {customerId: this.customerId}}).then(_ => {
    });
    }
  }

  dateOf(date: string): string {
    return moment(date).format('DD-MM-YYYY');
  }

  onBack(): void {
    if (this.catId != null) {
      this.router.navigate(['showCats']).then(_ => {
      });
    }
    else { this.router.navigate(['showCustomers']).then(_ => {
    });
    }
  }

}
