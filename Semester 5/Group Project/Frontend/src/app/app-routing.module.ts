import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginPageComponent } from "./account/login-page/login-page.component";
import { RegisterPageComponent } from "./account/register-page/register-page.component";
import { HomePageComponent } from "./games/home-page/home-page.component";
import { VerifyEmailComponent } from "./account/verify-email/verify-email.component";
import { StorePageComponent } from "./games/store-page/store-page.component";
import { UpdateProfileComponent } from "./profile/update-profile/update-profile.component";

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginPageComponent },
  { path: 'register', component: RegisterPageComponent },
  { path: 'verifyEmail', component: VerifyEmailComponent },
  { path: 'home', component: HomePageComponent },
  { path: 'store', component: StorePageComponent },
  { path: 'changeAccountDetails', component: UpdateProfileComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
