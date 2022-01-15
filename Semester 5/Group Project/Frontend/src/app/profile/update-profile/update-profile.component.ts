import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";

import { ProfileService } from "../../common/services/profile.service";
import { User } from "../../common/models/user.model";
import { Utils } from "../../common/utils";

@Component({
  selector: 'app-update-profile',
  templateUrl: './update-profile.component.html',
  styleUrls: ['./update-profile.component.css']
})
export class UpdateProfileComponent implements OnInit {
  username: string = '';
  user: User = {} as User;

  constructor(private router: Router, private profileService: ProfileService, public utils: Utils) { }

  ngOnInit(): void {
    this.username = String(localStorage.getItem('username'));

    this.profileService.getUserByUsername(this.username).subscribe(
      (user) => {
        if(user == null) { window.alert(`The user ${this.username} does not exist!`); }
        else { this.user = user; }
      });
  }

  updateProfile(currentPassword: string, newPassword: string, bio: string, lastName: string, firstName: string, email: string, avatar: string) {
    let updatedUser = new User(firstName, lastName, this.username, email, bio, newPassword, 'false', avatar);
    this.profileService.updateProfile(this.username, currentPassword, updatedUser).subscribe(hasChanged => {
      if (hasChanged) {
        localStorage.clear();
        this.router.navigate(['login']).then(_ => {});
      }
    });
  }
}
