import {Component, OnInit} from '@angular/core';
import {Service} from '../service';
import {ActivatedRoute, Router} from '@angular/router';
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-delete-user',
  templateUrl: './delete-user.component.html',
  styleUrls: ['./delete-user.component.css']
})
export class DeleteUserComponent implements OnInit {

  constructor(private service: Service, private router: Router, private route: ActivatedRoute, private cookieService: CookieService) {
  }

  ngOnInit(): void {
    if (this.cookieService.get("loggedIn") != "1")
      this.router.navigate(["login"]).then(_=>{});
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
