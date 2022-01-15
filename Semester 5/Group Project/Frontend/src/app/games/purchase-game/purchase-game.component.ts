import { Component, OnInit } from '@angular/core';
import {Game} from "../../common/models/game.model";
import {GameService} from "../../common/services/game.service";
import {ActivatedRoute, Router} from "@angular/router";
import {PurchaseService} from "../../common/services/purchase.service";
import {Purchase} from "../../common/models/purchase.model";
import {ProfileService} from "../../common/services/profile.service";

@Component({
  selector: 'app-purchase-game',
  templateUrl: './purchase-game.component.html',
  styleUrls: ['./purchase-game.component.css']
})
export class PurchaseGameComponent implements OnInit {

  title: string = '';
  game: Game = {} as Game;
  _1_31 = Array.from({length: 31}, (x, i) => i+1);
  _1_12 = Array.from({length: 12}, (x, i) => i+1);
  selectedDay: string = ""
  selectedMonth: string = ""


  constructor(private gameService: GameService, private purchaseService: PurchaseService, private profileService: ProfileService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
    this.title = this.route.snapshot.queryParams.title;
    this.gameService.getGameByTitle(this.title).subscribe((game) => {
      this.game = game;
    });
  }

  purchase(cardNumber: string, cvv: string, expDay: string, expMonth: string, name: string) {
    if (!cardNumber.match(/^[0-9]{16}$/g)) {
      alert("Card number is not valid")
      return
    }
    if (!cvv.match(/^[0-9]{3,4}$/g)) {
      alert("cvv is incorrect")
      return
    }
    if (!expDay || !expMonth) {
      alert("Invalid expiration date")
      return
    }
    if (name.length < 3) {
      alert("Name is too short")
      return
    }
    const username = localStorage.getItem('username')!!;
    this.profileService.getUserByUsername(username).subscribe(user => {
      this.purchaseService.addPurchase(new Purchase(new Date(), this.game.price, user, this.game)).subscribe(() => {
        this.router.navigate(['home']).then(() => {})
      })
    })
  }
}
