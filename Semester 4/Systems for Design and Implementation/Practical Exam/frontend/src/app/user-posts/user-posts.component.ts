import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-user-posts',
  templateUrl: './user-posts.component.html',
  styleUrls: ['./user-posts.component.css']
})
export class UserPostsComponent implements OnInit {
  username = "";
  // @ts-ignore
  user;
  followers = Array();
  posts = Array();

  constructor(private http: HttpClient, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.http.get("http://localhost:8080/api/user-posts/" + this.route.snapshot.paramMap.get("id")).subscribe(user => {
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
      // @ts-ignore
      this.posts = user.posts.sort((l, r)=>{
        if (l.title < r.title)
          return -1;
        if (l.title > r.title)
          return 1;
        return 0;
      })
      for (let post of this.posts)
        { // @ts-ignore
          post.comments = post.comments.sort((l, r)=>{
                    if (l.comment < r.comment)
                      return -1;
                    if (l.comment > r.comment)
                      return 1;
                    return 0;
                  })
        }
    })
  }

}
