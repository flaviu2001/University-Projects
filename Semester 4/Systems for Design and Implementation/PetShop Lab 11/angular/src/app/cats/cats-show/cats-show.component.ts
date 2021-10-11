import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {Cat} from "../cats-model";
import {CatsService} from "../cats-service";

@Component({
  selector: 'app-cats-show',
  templateUrl: './cats-show.component.html',
  styleUrls: ['./cats-show.component.css']
})
export class CatsShowComponent implements OnInit {

  cats: Array<Cat>;

  constructor(private service: CatsService, private router: Router) {
  }

  ngOnInit(): void {
    this.refresh("")
  }

  refresh(breed: string): void {
    this.service.getCatsByBreed(breed).subscribe(cats => this.cats = cats.cats);
  }

  navigateToDelete(catId: number): void {
    this.router.navigate(['deleteCat'], {queryParams: {id: catId}}).then(_ => {
    });
  }

  navigateToAdd(): void {
    this.router.navigate(['addCat']).then(_ => {
    });
  }

  navigateToUpdate(catId: number): void {
    this.router.navigate(['updateCat'], {queryParams: {id: catId}}).then(_ => {
    });
  }

  navigateToToys(catId: number): void {
    this.router.navigate(['showToys'], {queryParams: {id: catId}}).then(_ => {
    });
  }

  navigateToPurchases(catId: number): void {
    this.router.navigate(['showPurchases'], {queryParams: {catId: catId}}).then(_ => {
    });
  }

  onBack() {
    this.router.navigate(['switcher']).then(_ => {
    })
  }
}
