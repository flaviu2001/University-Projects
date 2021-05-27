import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ToysService} from '../toys-service';

@Component({
  selector: 'app-toys-delete',
  templateUrl: './toys-delete.component.html',
  styleUrls: ['./toys-delete.component.css']
})
export class ToysDeleteComponent implements OnInit {

  constructor(private service: ToysService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
  }

  onYes(): void {
    this.service.deleteToy(this.route.snapshot.queryParams.id).subscribe(() => {
      this.router.navigate(['showToys'], {queryParams: {id: this.route.snapshot.queryParams.catId}}).then(_ => {
      });
    });
  }

  onNo(): void {
    this.router.navigate(['showToys'], {queryParams: {id: this.route.snapshot.queryParams.catId}}).then(_ => {
    });
  }
}
