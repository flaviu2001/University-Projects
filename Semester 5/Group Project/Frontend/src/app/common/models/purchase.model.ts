import {User} from "./user.model";
import {Game} from "./game.model";

export class Purchase {
  id: number;
  date: Date;
  price: number;
  arcaneUserDto: User;
  gameDto: Game;

  constructor(date: Date, price: number, arcaneUserDto: User, gameDto: Game) {
    this.id = -1;
    this.date = date;
    this.price = price;
    this.arcaneUserDto = arcaneUserDto;
    this.gameDto = gameDto;
  }
}
