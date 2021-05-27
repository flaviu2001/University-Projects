import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-switcher',
  templateUrl: './switcher.component.html',
  styleUrls: ['./switcher.component.css']
})
export class SwitcherComponent implements OnInit {

  constructor(private router: Router) {
  }

  ngOnInit(): void {
  }

  onCats() {
    this.router.navigate(['showCats']).then(_ => {
    })
  }

  onFoods() {
    this.router.navigate(['showFoods']).then(_ => {
    })
  }

  onCustomers() {
    this.router.navigate(['showCustomers']).then(_ => {
    })
  }

  onReport() {
    this.router.navigate(['report']).then(_ => {
    })
  }
}
