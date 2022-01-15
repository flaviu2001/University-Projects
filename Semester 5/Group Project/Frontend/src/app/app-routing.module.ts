import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginPageComponent } from "./account/login-page/login-page.component";
import { RegisterPageComponent } from "./account/register-page/register-page.component";
import { HomePageComponent } from "./games/home-page/home-page.component";
import { VerifyEmailComponent } from "./account/verify-email/verify-email.component";
import { StorePageComponent } from "./games/store-page/store-page.component";
import { UpdateProfileComponent } from "./profile/update-profile/update-profile.component";
import { ViewProfileComponent } from "./profile/view-profile/view-profile.component";
import { InfoGameComponent } from "./games/info-game/info-game.component";
import { PurchaseGameComponent } from "./games/purchase-game/purchase-game.component";
import { FriendsPageComponent } from "./profile/friends-page/friends-page.component";
import { WishListComponent } from "./games/wish-list/wish-list.component";
import { MessagesBetweenFriendsComponent } from "./profile/messages-between-friends/messages-between-friends.component";
import { SearchUsersComponent } from "./profile/search-users/search-users.component";

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginPageComponent },
  { path: 'register', component: RegisterPageComponent },
  { path: 'verifyEmail', component: VerifyEmailComponent },
  { path: 'home', component: HomePageComponent },
  { path: 'store', component: StorePageComponent },
  { path: 'changeAccountDetails', component: UpdateProfileComponent },
  { path: 'profile/:name', component: ViewProfileComponent },
  { path: 'infoGame', component: InfoGameComponent },
  { path: 'purchaseGame', component: PurchaseGameComponent },
  { path: 'friends', component: FriendsPageComponent },
  { path: 'wishlist', component: WishListComponent },
  { path: 'conversation/:name', component: MessagesBetweenFriendsComponent },
  { path: 'searchUsers', component: SearchUsersComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
