import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FoodsService} from "../foods-service";
import * as moment from 'moment';

@Component({
  selector: 'app-foods-update',
  templateUrl: './foods-update.component.html',
  styleUrls: ['./foods-update.component.css']
})
export class FoodsUpdateComponent implements OnInit {

  name = ""
  producer = ""
  expiration_date = ""

  constructor(private service: FoodsService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.service.getFood(this.route.snapshot.queryParams.id).subscribe(food => {
      this.name = food.name
      this.producer = food.producer
      this.expiration_date = moment(food.expirationDate).format("YYYY-MM-DD")
    })
  }

  updateFood(data): void {
    if (!data.expiration_date) {
      alert("The expiration date cannot be empty")
      return
    }
    const id = this.route.snapshot.queryParams.id;
    this.service.updateFood(id, data.name, data.producer, data.expiration_date).subscribe(() => {
      this.router.navigate(['showFoods']).then(_ => {
      });
    });
  }

  onCancel(): void {
    this.router.navigate(['showFoods']).then(_ => {
    });
  }

}
