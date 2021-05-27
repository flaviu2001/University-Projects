import { Component, OnInit } from '@angular/core';
import {ReportDTO} from './report-model';
import {ReportService} from './report-service';
import {Router} from '@angular/router';
import {Customer} from '../customer/customers-model';
import {CustomersService} from '../customer/customers-service';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {

  pairs: Array<ReportDTO>;
  customers: Array<Customer> = null;

  constructor(private service: ReportService, private customerService: CustomersService, private router: Router) { }

  ngOnInit(): void {
    this.customerService.getCustomers().subscribe(customers => this.customers = customers);
    this.refresh();
  }

  refresh(): void {
    if (this.customers == null){
      setTimeout(() => {
        this.refresh();
      }, 100);
      return;
    }
    this.service.getReport().subscribe(report => {
      this.pairs = report.customersSpentCash;
      for (const pair of this.pairs) {
        // @ts-ignore
        pair.customerId = this.customers.find(customer => customer.id == pair.customerId).name;
      }
    });
  }

  onBack(): void {
    this.router.navigate(['switcher']).then(_ => {
    });
  }
}
