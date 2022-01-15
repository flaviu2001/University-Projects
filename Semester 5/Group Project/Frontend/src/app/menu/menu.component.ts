import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { Utils } from "../common/utils";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {
  username: String = '';

  constructor(private router: Router, public utils: Utils) { }

  ngOnInit(): void {
    this.username = String(localStorage.getItem('username'));
  }

  navigateToHomePage() {
    this.router.navigate(['home']).then(_ => {});
  }

  navigateToStorePage() {
    this.router.navigate(['store']).then(_ => {});
  }

  navigateToWishListPage() {
    this.router.navigate(['wishlist']).then(_ => {});
  }

  navigateToProfilePage() {
    this.router.navigate([`profile/${this.username}`]).then(_ => {});
  }

  navigateToFriendsPage() {
    this.router.navigate([`friends`]).then(_ => {});
  }

  navigateToSearchUsersPage() {
    this.router.navigate([`searchUsers`]).then(_ => {});
  }

  navigateToChangeAccountDetailsPage() {
    this.router.navigate(['changeAccountDetails']).then(_ => {});
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['login']).then(_ => {});
  }
}
