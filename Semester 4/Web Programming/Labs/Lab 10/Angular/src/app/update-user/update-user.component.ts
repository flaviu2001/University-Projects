import {Component, OnInit} from '@angular/core';
import {Service} from '../service';
import {ActivatedRoute, Router} from '@angular/router';
import {Observable} from "rxjs";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.css']
})
export class UpdateUserComponent implements OnInit {

  name = ""
  username = ""
  password = ""
  age = 0
  role = ""
  email = ""
  webpage = ""

  constructor(private service: Service, private router: Router, private route: ActivatedRoute, private cookieService: CookieService) {
  }

  ngOnInit(): void {
    if (this.cookieService.get("loggedIn") != "1")
      this.router.navigate(["login"]).then(_=>{});
    this.service.getUser(this.route.snapshot.queryParams.id).subscribe(user => {
      this.name = user.name
      this.username = user.username
      this.password = user.password
      this.age = user.age
      this.role = user.role
      this.email = user.email
      this.webpage = user.webpage
    })
  }

  updateUser(userForm: any): void {
    const id = this.route.snapshot.queryParams.id;
    this.service.updateUser(id,
      userForm.name,
      userForm.username,
      userForm.password,
      userForm.age,
      userForm.role,
      userForm.email,
      userForm.webpage
    ).subscribe(() => {
      this.router.navigate(['showUsers']).then(_ => {
      });
    });
  }

  onCancel(): void {
    this.router.navigate(['showUsers']).then(_ => {
    });
  }

}
