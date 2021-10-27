import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {FoodsService} from '../foods-service';

@Component({
  selector: 'app-foods-add',
  templateUrl: './foods-add.component.html',
  styleUrls: ['./foods-add.component.css']
})
export class FoodsAddComponent implements OnInit {

  constructor(private service: FoodsService, private router: Router) {
  }

  ngOnInit(): void {
  }

  addFood(data): void {
    if (!data.expiration_date) {
      alert('The expiration date cannot be empty');
      return;
    }
    console.log(data.expiration_date);
    this.service.addFood(data.name, data.producer, data.expiration_date).subscribe(() => {
      this.router.navigate(['showFoods']).then(_ => {
      });
    });
  }

  onCancel(): void {
    this.router.navigate(['showFoods']).then(_ => {
    });
  }
}
