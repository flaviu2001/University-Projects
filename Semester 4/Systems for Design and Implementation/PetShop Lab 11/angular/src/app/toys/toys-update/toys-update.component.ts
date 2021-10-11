import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ToysService} from "../toys-service";
import {CatsService} from "../../cats/cats-service";

@Component({
  selector: 'app-toys-update',
  templateUrl: './toys-update.component.html',
  styleUrls: ['./toys-update.component.css']
})
export class ToysUpdateComponent implements OnInit {

  name = ""
  price = ""

  constructor(private service: ToysService, private catService: CatsService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.service.getToy(this.route.snapshot.queryParams.id).subscribe(toy => {
      this.name = toy.name
      this.price = "" + toy.price
    })
  }

  updateToy(data): void {
    const id = this.route.snapshot.queryParams.id;
    const catId = this.route.snapshot.queryParams.catId;
    this.catService.getCat(catId).subscribe(cat => {
      this.service.updateToy(id, data.name, data.price, cat).subscribe(() => {
        this.router.navigate(['showToys'], {queryParams: {id: catId}}).then(_ => {
        });
      });
    })
  }

  onCancel(): void {
    this.router.navigate(['showToys'], {queryParams: {id: this.route.snapshot.queryParams.catId}}).then(_ => {
    });
  }

}
