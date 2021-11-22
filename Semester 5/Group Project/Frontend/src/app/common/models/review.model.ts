import { User } from "./user.model";
import { Game } from "./game.model";

export class Review {
  id: number;
  numberOfStars: number;
  text: string;
  arcaneUserDto: User;
  gameDto: Game;

  constructor(_numberOfStars: number, _text: string, _user: User, _game: Game) {
    this.id = -1;
    this.numberOfStars = _numberOfStars;
    this.text = _text;
    this.arcaneUserDto = _user;
    this.gameDto = _game;
  }
}
