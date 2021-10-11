import {Component, OnInit} from '@angular/core';
import {PurchasesService} from '../purchases-service';
import {ActivatedRoute, Router} from '@angular/router';
import * as moment from 'moment';
import {Cat} from '../../cats/cats-model';
import {Customer} from '../../customer/customers-model';
import {CatsService} from '../../cats/cats-service';
import {CustomersService} from '../../customer/customers-service';

@Component({
  selector: 'app-purchases-add',
  templateUrl: './purchases-add.component.html',
  styleUrls: ['./purchases-add.component.css']
})
export class PurchasesAddComponent implements OnInit {
  cats: Array<Cat>;
  customers: Array<Customer>;
  catId;
  customerId;
  price;
  dateAcquired;
  review;

  constructor(private service: PurchasesService, private catsService: CatsService, private customersService: CustomersService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
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
    if (this.catId != null) {
      this.customersService.getCustomers().subscribe(customers => this.customers = customers);
    }
    else { this.catsService.getCats().subscribe(cats => this.cats = cats.cats); }
  }

  addPurchase(id: number) {
    if (!this.price || !this.dateAcquired || !this.review) {
      alert('Invalid data');
      return;
    }
    this.service.getPurchases(this.catId, this.customerId).subscribe(purchases => {
      let currentCatId = this.catId;
      let currentCustomerId = this.customerId;
      if (currentCatId == null) {
        currentCatId = id;
      }
      if (currentCustomerId == null) {
        currentCustomerId = id;
      }
      if (purchases.find(purchase => purchase.cat.id == currentCatId && purchase.customer.id == currentCustomerId)) {
        alert('The pair is already present in the database');
        return;
      }
      if (this.catId == null) {
        this.service.addPurchase(this.price, this.dateAcquired, this.review, id, this.customerId).subscribe(() => {
          this.onBack();
        });
      } else {
        this.service.addPurchase(this.price, this.dateAcquired, this.review, this.catId, id).subscribe(() => {
          this.onBack();
        });
      }
    });
  }

  dateOf(date: string) {
    return moment(date).format('DD-MM-YYYY');
  }

  onBack() {
    if (this.catId != null) {
      this.router.navigate(['showPurchases'], {queryParams: {catId: this.catId}}).then(_ => {
      });
    }
    else { this.router.navigate(['showPurchases'], {queryParams: {customerId: this.customerId}}).then(_ => {
    });
    }
  }
}
