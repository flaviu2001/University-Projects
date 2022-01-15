import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";

import { ProfileService } from "../../common/services/profile.service";
import { Utils } from "../../common/utils";
import { User } from "../../common/models/user.model";
import { GameService } from "../../common/services/game.service";
import { Game } from "../../common/models/game.model";
import { Review } from "../../common/models/review.model";
import { ReviewService } from "../../common/services/review.service";
import {FriendsService} from "../../common/services/friends.service";
import {FriendInvitation} from "../../common/models/friend-invitation.model";

@Component({
  selector: 'app-view-profile',
  templateUrl: './view-profile.component.html',
  styleUrls: ['./view-profile.component.css']
})
export class ViewProfileComponent implements OnInit {

  username: string = '';
  user: User = {} as User;
  games: Game[] = [];
  reviews: Review[] = [];
  isFriend: boolean = false;

  constructor(private router: Router, private route: ActivatedRoute,
              private profileService: ProfileService,
              private gameService: GameService,
              private reviewService: ReviewService,
              private friendsService: FriendsService,
              public utils: Utils) { }

  ngOnInit(): void {
    this.username = this.route.snapshot.params.name;
    this.profileService.getUserByUsername(this.username).subscribe(
      (user) => {
        if(user == null) { window.alert(`The user ${this.username} does not exist!`); }
        else { this.user = user; }
      });
    this.gameService.getGamesFromUser(this.username).subscribe(
      (games) => {
        this.games = games;
      }
    )
    this.reviewService.getReviewsOfUser(this.username).subscribe( reviews => {
        this.reviews = reviews;
      }
    )
    this.friendsService.getAll(localStorage.getItem("username")!!).subscribe(friends => {
      for (let friend of friends)
        if (friend.invitedDto.userName == this.username || friend.inviterDto.userName == this.username)
          this.isFriend = true;
    })
  }

  goToInfoGamePage(title: string) {
    this.router.navigate(['infoGame'], {
      queryParams: {
        title: title
      }
    }).then(_ => {});
  }

  addFriend() {
    this.profileService.getUserByUsername(localStorage.getItem("username")!!).subscribe(inviter => {
      this.profileService.getUserByUsername(this.username).subscribe(invited => {
        this.friendsService.add(new FriendInvitation(inviter, invited, "PENDING")).subscribe(_ => {
          window.location.reload()
        })
      })
    })
  }
}
