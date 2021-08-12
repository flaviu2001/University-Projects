import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-user-hello',
  templateUrl: './user-hello.component.html',
  styleUrls: ['./user-hello.component.css']
})
export class UserHelloComponent implements OnInit {
  username = "";

  constructor(private http: HttpClient, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.http.post("http://localhost:8080/api/user-hello/" + this.route.snapshot.paramMap.get("id"), {}).subscribe((user) => {
      // console.log(user)
      // @ts-ignore
      this.username = user.name;
    })
  }

}
