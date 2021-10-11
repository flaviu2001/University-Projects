import {Customer} from "../customer/customers-model";

export class ReportDTO {
  totalCash: number;
  customer: Customer;


  constructor(totalCash: number = 0, customer: Customer = null) {
    this.totalCash = totalCash;
    this.customer = customer;
  }
}

export class ReportsDTO {
  customersSpentCash: Array<ReportDTO>;
}
