import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  backend = "http://localhost:42020/login"

  constructor(private http: HttpClient, private router: Router) {
  }

  ngOnInit(): void {
  }

  login(name: string): void {
    this.http.get(this.backend + `?name=${name}`).subscribe(number => {
      if (number == 1) {
        this.router.navigate([`main/${name}`]).then(() => {
        })
      } else alert("Wrong username!")
    })
  }
}
