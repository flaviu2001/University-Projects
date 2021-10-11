export class Customer {
  id: number;
  name: string;
  phoneNumber: string;
  petHouseDTO;

  constructor(name: string = "", phoneNumber: string = "", petHouseDTO = null) {
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.petHouseDTO = petHouseDTO;
  }
}

export class CustomersDTO {
  customers: Array<Customer>
}
