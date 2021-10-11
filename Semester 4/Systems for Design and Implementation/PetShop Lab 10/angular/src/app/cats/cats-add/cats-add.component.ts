import {Component, OnInit} from '@angular/core';
import {CatsService} from "../cats-service";
import {Router} from "@angular/router";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-cats-add',
  templateUrl: './cats-add.component.html',
  styleUrls: ['./cats-add.component.css']
})
export class CatsAddComponent implements OnInit {
  addCatForm = new FormGroup({
    name: new FormControl('', Validators.required),
    breed: new FormControl('', Validators.required),
    catYears: new FormControl('', Validators.required),
  })

  constructor(private service: CatsService, private router: Router) {
  }

  ngOnInit(): void {
  }

  addCat(): void {
    if (this.addCatForm.get("name").invalid) {
      alert("The cat name cannot be empty")
      return
    }
    if (this.addCatForm.get("breed").invalid) {
      alert("The cat breed cannot be empty")
      return
    }
    if (this.addCatForm.get("catYears").invalid) {
      alert("The cat age cannot be empty")
      return
    }
    this.service.addCat(this.addCatForm.get("name").value, this.addCatForm.get("breed").value, this.addCatForm.get("catYears").value).subscribe(() => {
      this.router.navigate(['showCats']).then(_ => {
      });
    });
  }

  onCancel(): void {
    this.router.navigate(['showCats']).then(_ => {
    });
  }

}
