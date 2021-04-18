import { Component, OnInit } from '@angular/core';
import {GenericService} from '../generic.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.css']
})
export class UpdateUserComponent implements OnInit {

  constructor(private service: GenericService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
  }

  updateUser(name: string, username: string, password: string, age: string, role: string, email: string, webpage: string): void {
    const id = this.route.snapshot.queryParams.id;
    this.service.updateUser(id, name, username, password, Number(age), role, email, webpage).subscribe(() => {
      this.router.navigate(['showUsers']).then(_ => {
      });
    });
  }

  onCancel(): void {
    this.router.navigate(['showUsers']).then(_ => {
    });
  }

}
