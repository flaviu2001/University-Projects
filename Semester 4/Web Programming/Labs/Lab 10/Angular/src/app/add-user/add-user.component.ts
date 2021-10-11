import {Component, OnInit} from '@angular/core';
import {Service} from '../service';
import {Router} from '@angular/router';
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {

  constructor(private service: Service, private router: Router, private cookieService: CookieService) {
  }

  ngOnInit(): void {
    if (this.cookieService.get("loggedIn") != "1")
      this.router.navigate(["login"]).then(_=>{});
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
