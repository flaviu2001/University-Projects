import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UserHelloComponent } from './user-hello/user-hello.component';
import { UserFollowersComponent } from './user-followers/user-followers.component';
import { UserPostsComponent } from './user-posts/user-posts.component';

@NgModule({
  declarations: [
    AppComponent,
    UserHelloComponent,
    UserFollowersComponent,
    UserPostsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
