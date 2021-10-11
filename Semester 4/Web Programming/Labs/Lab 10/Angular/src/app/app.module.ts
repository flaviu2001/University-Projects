import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ShowUsersComponent} from './show-users/show-users.component';
import { DeleteUserComponent } from './delete-user/delete-user.component';
import { AddUserComponent } from './add-user/add-user.component';
import { UpdateUserComponent } from './update-user/update-user.component';
import {FormsModule} from "@angular/forms";
import { LoginComponent } from './login/login.component';
import { CookieService } from 'ngx-cookie-service';

@NgModule({
  declarations: [
    AppComponent,
    ShowUsersComponent,
    DeleteUserComponent,
    AddUserComponent,
    UpdateUserComponent,
    LoginComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        FormsModule
    ],
  providers: [CookieService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
