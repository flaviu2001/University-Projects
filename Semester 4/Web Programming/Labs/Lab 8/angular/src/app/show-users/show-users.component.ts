import {Component, OnInit} from '@angular/core';
import {GenericService} from '../generic.service';
import {User} from '../user';
import {Router} from '@angular/router';

@Component({
  selector: 'app-show-users',
  templateUrl: './show-users.component.html',
  styleUrls: ['./show-users.component.css']
})
export class ShowUsersComponent implements OnInit {

  users: Array<User>;

  constructor(private service: GenericService, private router: Router) {
  }

  ngOnInit(): void {
    this.refresh('', '');
  }

  refresh(role: string, name: string): void {
    this.service.fetchUsers(role, name).subscribe(users => this.users = users);
  }

  navigateToDelete(userId: number): void {
    this.router.navigate(['deleteUser'], {queryParams: {id: userId}}).then(_ => {
    });
  }

  navigateToAdd(): void {
    this.router.navigate(['addUser']).then(_ => {
    });
  }

  navigateToUpdate(userId: number): void {
    this.router.navigate(['updateUser'], {queryParams: {id: userId}}).then(_ => {
    });
  }

}
