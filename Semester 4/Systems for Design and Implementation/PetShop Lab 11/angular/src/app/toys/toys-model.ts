export class Toy {
  id: number;
  name: string;
  price: number;
  cat;

  constructor(name: string = "", price: number = 0, cat = null) {
    this.name = name;
    this.price = price;
    this.cat = cat;
  }
}

export class ToysDTO {
  toys: Array<Toy>
}
