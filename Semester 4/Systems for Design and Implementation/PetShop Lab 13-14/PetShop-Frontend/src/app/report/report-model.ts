import {Purchase} from '../purchases/purchases-model';

export class ReportDTO {
  totalCash: number;
  customerId: number;


  constructor(totalCash: number = 0, customerId: number = 0) {
    this.totalCash = totalCash;
    this.customerId = customerId;
  }
}

export class ReportsDTO {
  customersSpentCash: Array<ReportDTO>;
}
