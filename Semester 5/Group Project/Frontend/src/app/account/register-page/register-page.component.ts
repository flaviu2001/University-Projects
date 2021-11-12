import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";

import { LoginRegisterService } from "../../common/services/login-register.service";
import { User } from "../../common/models/user.model";
import { Utils } from "../../common/utils";

@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrls: ['./register-page.component.css']
})
export class RegisterPageComponent implements OnInit {
  constructor(private router: Router, private registerService: LoginRegisterService,
              public utils: Utils) { }

  ngOnInit(): void {
  }

  register(firstName: string, lastName: string, username: string, email: string, bio: string,
           password: string, reenterPassword: string): void {

    if(password != reenterPassword) {
      window.alert("The passwords do not match!");
      return;
    }

    let user = new User(firstName, lastName, username, email, bio, password, 'false');
    this.registerService.register(user).subscribe(
      () => {
        this.goToVerifyEmailPage(username);
      });
  }

  goToVerifyEmailPage(username: String) {
    this.router.navigate(['verifyEmail'], {
      queryParams: {
        username: username
      }
    }).then(_ => {});
  }

  goToLoginPage() {
    this.router.navigate(['login']).then(_ => {});
  }
}
