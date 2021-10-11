import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Toy} from '../toys-model';
import {ToysService} from '../toys-service';

@Component({
  selector: 'app-toys-show',
  templateUrl: './toys-show.component.html',
  styleUrls: ['./toys-show.component.css']
})
export class ToysShowComponent implements OnInit {
  toys: Array<Toy>;

  constructor(private service: ToysService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.service.getToysOfCat(this.route.snapshot.queryParams.id).subscribe(toys => this.toys = toys.toys);
  }

  navigateToDelete(toysId: number): void {
    this.router.navigate(['deleteToy'], {queryParams: {id: toysId, catId: this.route.snapshot.queryParams.id}}).then(_ => {
    });
  }

  navigateToAdd(): void {
    this.router.navigate(['addToy'], {queryParams: {id: this.route.snapshot.queryParams.id}}).then(_ => {
    });
  }

  navigateToUpdate(toyId: number): void {
    this.router.navigate(['updateToy'], {queryParams: {id: toyId, catId: this.route.snapshot.queryParams.id}}).then(_ => {
    });
  }

  onBack(): void {
    this.router.navigate(['showCats']).then(_ => {
    });
  }
}
