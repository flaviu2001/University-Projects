import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CatsService} from "../cats-service";

@Component({
  selector: 'app-cats-update',
  templateUrl: './cats-update.component.html',
  styleUrls: ['./cats-update.component.css']
})
export class CatsUpdateComponent implements OnInit {

  name = ""
  breed = ""
  catYears = 0

  constructor(private service: CatsService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.service.getCat(this.route.snapshot.queryParams.id).subscribe(cat => {
      this.name = cat.name
      this.breed = cat.breed
      this.catYears = cat.catYears
    })
  }

  updateCat(data): void {
    const id = this.route.snapshot.queryParams.id;
    this.service.updateCat(id, data.name, data.breed, data.catYears).subscribe(() => {
      this.router.navigate(['showCats']).then(_ => {
      });
    });
  }

  onCancel(): void {
    this.router.navigate(['showCats']).then(_ => {
    });
  }


}
