import { Component, OnInit } from '@angular/core';
import {Service} from "../service";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private service: Service, private router: Router, private cookieService: CookieService) { }

  ngOnInit(): void {
  }

  handleLogin(form: any) {
    this.service.login(form.username, form.password).subscribe(thing=> {
      if (thing != null) {
        this.cookieService.set("loggedIn", "1");
        this.router.navigate(['showUsers']).then(_=>{});
      } else alert("Invalid credentials!")
    });
  }

}
