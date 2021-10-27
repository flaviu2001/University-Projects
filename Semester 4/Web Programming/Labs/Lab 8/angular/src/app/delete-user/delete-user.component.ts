import {Component, OnInit} from '@angular/core';
import {GenericService} from '../generic.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-delete-user',
  templateUrl: './delete-user.component.html',
  styleUrls: ['./delete-user.component.css']
})
export class DeleteUserComponent implements OnInit {

  constructor(private service: GenericService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
  }

  onYes(): void {
    this.service.deleteUser(this.route.snapshot.queryParams.id).subscribe(() => {
      this.router.navigate(['showUsers']).then(_ => {
      });
    });
  }

  onNo(): void {
    this.router.navigate(['showUsers']).then(_ => {
    });
  }

}
