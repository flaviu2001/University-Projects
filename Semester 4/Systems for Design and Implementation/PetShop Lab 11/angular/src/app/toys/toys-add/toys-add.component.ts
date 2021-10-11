import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ToysService} from "../toys-service";
import {CatsService} from "../../cats/cats-service";

@Component({
  selector: 'app-toys-add',
  templateUrl: './toys-add.component.html',
  styleUrls: ['./toys-add.component.css']
})
export class ToysAddComponent implements OnInit {

  constructor(private service: ToysService, private catService: CatsService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
  }

  addToy(data): void {
    this.catService.getCat(this.route.snapshot.queryParams.id).subscribe(cat => {
      this.service.addToy(data.name, data.price, cat).subscribe(() => {
        this.router.navigate(['showToys'], {queryParams: {id: this.route.snapshot.queryParams.id}}).then(_ => {
        });
      });
    })
  }

  onCancel(): void {
    this.router.navigate(['showToys'], {queryParams: {id: this.route.snapshot.queryParams.id}}).then(_ => {
    });
  }
}
