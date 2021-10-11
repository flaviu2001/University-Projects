import {Component, OnInit} from '@angular/core';
import {Cat} from "../shared/cats.model";
import {CatService} from "../shared/cats.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-cat-list',
  templateUrl: './cat-list.component.html',
  styleUrls: ['./cat-list.component.css']
})
export class CatListComponent implements OnInit {
  errorMessage: string;
  cats: Array<Cat>;
  selectedCat: Cat;

  constructor(private catService: CatService,
              private router: Router) {
  }


  ngOnInit(): void {
    this.getCats()
  }

  getCats() {
    this.catService.getCats()
      .subscribe(
        cats => {
          this.cats = cats.cats
          console.log(cats)
        },
        error => this.errorMessage = <any>error
      );
  }

  onSelect(cat: Cat): void {
    this.selectedCat = cat;
  }
}
