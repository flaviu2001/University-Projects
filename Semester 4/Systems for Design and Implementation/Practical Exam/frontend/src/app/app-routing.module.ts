import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {UserHelloComponent} from "./user-hello/user-hello.component";
import {UserFollowersComponent} from "./user-followers/user-followers.component";
import {UserPostsComponent} from "./user-posts/user-posts.component";

const routes: Routes = [
  // {path: '', redirectTo: 'user-', pathMatch: 'full'},
  {path: 'blog/user-hello/:id', component: UserHelloComponent},
  {path: 'blog/user-followers/:id', component: UserFollowersComponent},
  {path: 'blog/user-posts/:id', component: UserPostsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
