import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-user-followers',
  templateUrl: './user-followers.component.html',
  styleUrls: ['./user-followers.component.css']
})
export class UserFollowersComponent implements OnInit {
  username = "";
  followers = Array();

  constructor(private http: HttpClient, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.http.get("http://localhost:8080/api/user-followers/" + this.route.snapshot.paramMap.get("id")).subscribe(user=>{
      // @ts-ignore
      this.username = user.name
      // @ts-ignore
      this.followers = user.followers.sort((l, r)=>{
        if (l.name < r.name)
          return -1;
        if (l.name > r.name)
          return 1;
        return 0;
      })
    })
  }

}
