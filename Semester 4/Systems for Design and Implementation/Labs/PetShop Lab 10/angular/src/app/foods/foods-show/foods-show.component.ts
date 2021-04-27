import {Component, OnInit} from '@angular/core';
import {FoodsService} from "../foods-service";
import {Router} from "@angular/router";
import {Food} from "../foods-model";
import * as moment from 'moment';

@Component({
  selector: 'app-foods-show',
  templateUrl: './foods-show.component.html',
  styleUrls: ['./foods-show.component.css']
})
export class FoodsShowComponent implements OnInit {

  foods: Array<Food>;

  constructor(private service: FoodsService, private router: Router) {
  }

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.service.getFoods().subscribe(foods => this.foods = foods.foods);
  }

  navigateToDelete(catId: number): void {
    this.router.navigate(['deleteFood'], {queryParams: {id: catId}}).then(_ => {
    });
  }

  navigateToAdd(): void {
    this.router.navigate(['addFood']).then(_ => {
    });
  }

  navigateToUpdate(catId: number): void {
    this.router.navigate(['updateFood'], {queryParams: {id: catId}}).then(_ => {
    });
  }

  dateOf(date: string) {
    return moment(date).format("DD-MM-YYYY");
  }

  onBack() {
    this.router.navigate(['switcher']).then(_ => {
    })
  }

}
