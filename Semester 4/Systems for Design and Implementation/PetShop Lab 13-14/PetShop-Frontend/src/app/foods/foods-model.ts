export class Food {
  id: number;
  name: string;
  producer: string;
  expirationDate: string;

  constructor(name: string = "", producer: string = "", expirationDate: string = "") {
    this.name = name;
    this.producer = producer;
    this.expirationDate = expirationDate;
  }
}
