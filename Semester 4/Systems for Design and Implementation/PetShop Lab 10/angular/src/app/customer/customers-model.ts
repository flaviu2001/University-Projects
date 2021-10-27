export class Customer {
  id: number;
  name: string;
  phoneNumber: string;

  constructor(name: string = "", phoneNumber: string = "") {
    this.name = name;
    this.phoneNumber = phoneNumber;
  }
}

export class CustomersDTO {
  customers: Array<Customer>
}
