import { Component, OnInit } from '@angular/core';
import {CatsService} from "../cats-service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-cats-delete',
  templateUrl: './cats-delete.component.html',
  styleUrls: ['./cats-delete.component.css']
})
export class CatsDeleteComponent implements OnInit {

  constructor(private service: CatsService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
  }

  onYes(): void {
    this.service.deleteCat(this.route.snapshot.queryParams.id).subscribe(() => {
      this.router.navigate(['showCats']).then(_ => {
      });
    });
  }

  onNo(): void {
    this.router.navigate(['showCats']).then(_ => {
    });
  }

}
