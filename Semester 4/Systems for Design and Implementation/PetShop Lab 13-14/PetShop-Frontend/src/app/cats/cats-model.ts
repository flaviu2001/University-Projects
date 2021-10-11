export class Cat {
  id: number;
  name: string;
  breed: string;
  catYears: number;

  constructor(name: string = '', breed: string = '', catYears: number = 0) {
    this.name = name;
    this.breed = breed;
    this.catYears = catYears;
  }
}

export class CatsDTO {
  cats: Array<Cat>
}
