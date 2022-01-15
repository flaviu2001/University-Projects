import { Component, OnInit } from '@angular/core';
import { PageEvent } from "@angular/material/paginator";
import { Router } from "@angular/router";

import { User } from "../../common/models/user.model";
import { ProfileService } from "../../common/services/profile.service";

@Component({
  selector: 'app-search-users',
  templateUrl: './search-users.component.html',
  styleUrls: ['./search-users.component.css']
})
export class SearchUsersComponent implements OnInit {
  pageEvent: PageEvent | undefined;
  pagedList: User[] = [];

  users: User[] = [];

  constructor(private profileService: ProfileService, private router: Router) { }

  ngOnInit(): void {
    this.searchUsers('');
  }

  searchUsers(username: string) {
    this.profileService.searchUsers(username).subscribe(
      (users) => {
        this.users = users;
        this.pagedList = this.users.slice(0, 3);
      }
    );
  }

  handleCheckout(user: User) {
    this.router.navigate([`profile/${user.userName}`]).then(_ => {});
  }

  onPaginateChange(event: PageEvent) {
    let startIndex = event.pageIndex * event.pageSize;
    let endIndex = startIndex + event.pageSize;

    if(endIndex > this.users.length){
      endIndex = this.users.length;
    }

    this.pagedList = this.users.slice(startIndex, endIndex);
  }
}
