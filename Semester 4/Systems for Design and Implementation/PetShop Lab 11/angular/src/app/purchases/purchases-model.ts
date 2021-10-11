import {Cat} from "../cats/cats-model";
import {Customer} from "../customer/customers-model";

export class Purchase {
  id;
  price: number;
  dateAcquired: string;
  review: number;
  cat: Cat;
  customer: Customer;


  constructor(price: number = 0, dateAcquired: string = "", review: number = 0, cat: Cat = null, customer: Customer = null) {
    this.price = price;
    this.dateAcquired = dateAcquired;
    this.review = review;
    this.cat = cat;
    this.customer = customer;
    this.id = {
      catId: cat.id,
      customerId: customer.id
    }
  }
}

export class PurchaseAddDTO {
  price: number;
  dateAcquired: string;
  review: number;
  catId: number;
  customerId: number;


  constructor(price: number, dateAcquired: string, review: number, catId: number, customerId: number) {
    this.price = price;
    this.dateAcquired = dateAcquired;
    this.review = review;
    this.catId = catId;
    this.customerId = customerId;
  }
}

export class PurchasesDTO {
  purchases: Array<Purchase>
}
