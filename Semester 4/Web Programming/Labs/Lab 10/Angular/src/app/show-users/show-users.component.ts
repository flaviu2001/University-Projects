import {Component, OnInit} from '@angular/core';
import {Service} from '../service';
import {User} from '../user';
import {Router} from '@angular/router';
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-show-users',
  templateUrl: './show-users.component.html',
  styleUrls: ['./show-users.component.css']
})
export class ShowUsersComponent implements OnInit {

  users: Array<User> = Array();
  previousRole = ""
  previousName = ""
  currentRole = ""
  currentName = ""

  constructor(private service: Service, private router: Router, private cookieService: CookieService) {
  }

  ngOnInit(): void {
    if (this.cookieService.get("loggedIn") != "1")
      this.router.navigate(["login"]).then(_=>{});
    this.refresh('', '');
  }

  refresh(role: string, name: string): void {
    this.previousRole = this.currentRole
    this.previousName = this.currentName
    this.currentRole = role
    this.currentName = name
    this.service.getUsers(role, name).subscribe(users => this.users = users);
  }

  navigateToDelete(userId: number): void {
    this.router.navigate(['deleteUser'], {queryParams: {id: userId}}).then(_ => {
    });
  }

  navigateToAdd(): void {
    this.router.navigate(['addUser']).then(_ => {
    });
  }

  navigateToUpdate(userId: number): void {
    this.router.navigate(['updateUser'], {queryParams: {id: userId}}).then(_ => {
    });
  }

  logout() {
    this.cookieService.delete("loggedIn");
    this.router.navigate(["login"]).then(_=>{});
  }

}
