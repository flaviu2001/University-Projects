import { Component, OnInit } from '@angular/core';
import {CatsService} from "../../cats/cats-service";
import {ActivatedRoute, Router} from "@angular/router";
import {FoodsService} from "../foods-service";

@Component({
  selector: 'app-foods-delete',
  templateUrl: './foods-delete.component.html',
  styleUrls: ['./foods-delete.component.css']
})
export class FoodsDeleteComponent implements OnInit {

  constructor(private service: FoodsService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
  }

  onYes(): void {
    this.service.deleteFood(this.route.snapshot.queryParams.id).subscribe(() => {
      this.router.navigate(['showFoods']).then(_ => {
      });
    });
  }

  onNo(): void {
    this.router.navigate(['showFoods']).then(_ => {
    });
  }

}
