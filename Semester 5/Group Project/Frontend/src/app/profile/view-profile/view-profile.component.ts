import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ProfileService} from "../../common/services/profile.service";
import {Utils} from "../../common/utils";
import {User} from "../../common/models/user.model";
import {GameService} from "../../common/services/game.service";
import {Game} from "../../common/models/game.model";

@Component({
  selector: 'app-view-profile',
  templateUrl: './view-profile.component.html',
  styleUrls: ['./view-profile.component.css']
})
export class ViewProfileComponent implements OnInit {
  username: string = '';
  user: User = {} as User;
  games: Game[] = [];

  constructor(private router: Router, private route: ActivatedRoute, private profileService: ProfileService, private gameService: GameService, public utils: Utils) { }

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
  }

  goToInfoGamePage(title: string) {
    this.router.navigate(['infoGame'], {
      queryParams: {
        title: title
      }
    }).then(_ => {});
  }
}
