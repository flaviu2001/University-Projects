export class Cat {
  id: number;
  name: string;
  breed: string;
  catYears: number;
}

export class CatsDTO {
  cats: Array<Cat>;
}
