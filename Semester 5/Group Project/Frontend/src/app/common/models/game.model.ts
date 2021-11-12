export class Game {
  id: number;
  title: string;
  description: string;
  price: number;
  pictureUrl: string;

  constructor(_title: string, _description: string, _price: number, _pictureUrl: string) {
    this.id = -1;
    this.title = _title;
    this.description = _description;
    this.price = _price;
    this.pictureUrl = _pictureUrl;
  }
}
