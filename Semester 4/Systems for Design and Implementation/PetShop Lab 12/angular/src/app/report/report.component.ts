import { Component, OnInit } from '@angular/core';
import {ReportDTO} from "./report-model";
import {ReportService} from "./report-service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {

  pairs: Array<ReportDTO>

  constructor(private service: ReportService, private router: Router) { }

  ngOnInit(): void {
    this.service.getReport().subscribe(report => this.pairs = report.customersSpentCash);
  }

  onBack() {
    this.router.navigate(['switcher']).then(_ => {
    })
  }
}
