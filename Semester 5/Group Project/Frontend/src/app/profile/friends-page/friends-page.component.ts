import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {FriendsService} from "../../common/services/friends.service";
import {FriendInvitation} from "../../common/models/friend-invitation.model";
import {User} from "../../common/models/user.model";

@Component({
  selector: 'app-friends-page',
  templateUrl: './friends-page.component.html',
  styleUrls: ['./friends-page.component.css']
})
export class FriendsPageComponent implements OnInit {
  username: string = ''
  friendInvites: FriendInvitation[] = []

  constructor(private router: Router, private friendsService: FriendsService) { }

  ngOnInit(): void {
    this.username = localStorage.getItem('username')!!;
    this.friendsService.getAllFriends(this.username).subscribe(friends => {
      this.friendInvites = friends.sort((a, b) => {
        const x = a.status == "PENDING" ? -1 : 1;
        const y = b.status == "PENDING" ? -1 : 1;
        if (x < y)
          return -1
        if (x == y)
          return 0
        return 1
      })
    })
  }

  getUser(friendInvitation: FriendInvitation): User {
    if (friendInvitation.status == "ACCEPTED") {
      if (friendInvitation.invitedDto.userName == this.username)
        return friendInvitation.inviterDto
      return friendInvitation.invitedDto
    }
    return friendInvitation.inviterDto
  }

  handleAccept(friendInvitation: FriendInvitation) {
    this.friendsService.accept(friendInvitation).subscribe(_ => {
      window.location.reload();
    })
  }

  handleReject(friendInvitation: FriendInvitation) {
    this.friendsService.reject(friendInvitation).subscribe(_ => {
      window.location.reload();
    })
  }

  handleCheckout(user: User) {
    this.router.navigate([`profile/${user.userName}`]).then(_ => {});
  }

  goToConversationPage(user: User) {
    this.router.navigate([`conversation/${user.userName}`]).then(_ => {});
  }
}
