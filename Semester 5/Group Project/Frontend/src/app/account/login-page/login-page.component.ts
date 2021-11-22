import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";

import { LoginRegisterService } from "../../common/services/login-register.service";

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {
  hide: boolean = true;

  constructor(private router: Router, private loginService: LoginRegisterService) {}

  ngOnInit(): void {
  }

  login(username: string, password: string): void {
    this.loginService.login(username, password).subscribe(
      (user) => {
        if(user) {
          if (user.emailVerified == "true") {
            this.goToHomePage();
            localStorage.setItem('username', username);
          }
          else this.goToVerifyPage(username);
        } else {
          window.alert("This user does not exist!");
        }
      });
  }

  goToRegisterPage() {
    this.router.navigate(['register']).then(_ => {});
  }

  goToHomePage() {
    this.router.navigate(['home']).then(_ => {});
  }

  goToVerifyPage(username: string) {
    this.router.navigate(['verifyEmail'], {
      queryParams: {
        username: username
      }
    }).then(_ => {});
  }
}
