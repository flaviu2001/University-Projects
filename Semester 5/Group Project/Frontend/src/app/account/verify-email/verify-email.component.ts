import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from "@angular/router";

import { LoginRegisterService } from "../../common/services/login-register.service";

@Component({
  selector: 'app-verify-email',
  templateUrl: './verify-email.component.html',
  styleUrls: ['./verify-email.component.css']
})
export class VerifyEmailComponent implements OnInit {
  username: string = '';

  constructor(private router: Router, private route: ActivatedRoute,
              private registerService: LoginRegisterService) { }

  ngOnInit(): void {
    this.username = this.route.snapshot.queryParams.username;
  }

  verifyEmail(code: string): void {
    this.registerService.verifyEmail(this.username, +code).subscribe(
      (ok) => {
        if(ok) {
          localStorage.setItem('username', this.username);
          this.goToHomePage();
        } else {
          window.alert("The verification code is wrong. Try again.");
        }
      })
  }

  goToHomePage() {
    this.router.navigate(['home']).then(_ => {});
  }
}
