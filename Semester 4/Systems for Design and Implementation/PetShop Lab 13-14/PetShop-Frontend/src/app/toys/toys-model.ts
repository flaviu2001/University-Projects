export class Toy {
  id: number;
  name: string;
  price: number;
  catId: number;

  constructor(name: string = '', price: number = 0, catId = 0) {
    this.name = name;
    this.price = price;
    this.catId = catId;
  }
}

export class ToysDTO {
  toys: Array<Toy>;
}
