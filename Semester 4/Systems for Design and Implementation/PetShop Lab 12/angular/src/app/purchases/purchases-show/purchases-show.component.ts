import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import * as moment from "moment";
import {Purchase} from "../purchases-model";
import {PurchasesService} from "../purchases-service";

@Component({
  selector: 'app-purchases-show',
  templateUrl: './purchases-show.component.html',
  styleUrls: ['./purchases-show.component.css']
})
export class PurchasesShowComponent implements OnInit {
  purchases: Array<Purchase>;
  catId;
  customerId;

  constructor(private service: PurchasesService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.catId = this.route.snapshot.queryParams.catId
    this.customerId = this.route.snapshot.queryParams.customerId
    if (typeof this.catId === "undefined")
      this.catId = null
    if (typeof this.customerId === "undefined")
      this.customerId = null
    this.service.getPurchases(this.catId, this.customerId).subscribe(purchases => this.purchases = purchases);
  }

  navigateToAdd(): void {
    if (this.catId != null)
      this.router.navigate(['addPurchase'], {queryParams: {catId: this.catId}}).then(_ => {
      });
    else this.router.navigate(['addPurchase'], {queryParams: {customerId: this.customerId}}).then(_ => {
    });
  }

  dateOf(date: string) {
    return moment(date).format("DD-MM-YYYY");
  }

  onBack() {
    if (this.catId != null)
      this.router.navigate(['showCats']).then(_ => {
      })
    else this.router.navigate(['showCustomers']).then(_ => {
    })
  }

}
