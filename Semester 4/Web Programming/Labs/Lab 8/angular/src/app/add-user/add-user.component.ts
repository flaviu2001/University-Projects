import {Component, OnInit} from '@angular/core';
import {GenericService} from '../generic.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {

  constructor(private service: GenericService, private router: Router) {
  }

  ngOnInit(): void {
  }

  addUser(name: string, username: string, password: string, age: string, role: string, email: string, webpage: string): void {
    this.service.addUser(name, username, password, Number(age), role, email, webpage).subscribe(() => {
      this.router.navigate(['showUsers']).then(_ => {
      });
    });
  }

  onCancel(): void {
    this.router.navigate(['showUsers']).then(_ => {
    });
  }

}
