import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from "@angular/common/http";

import { LoginPageComponent } from './account/login-page/login-page.component';
import { RegisterPageComponent } from './account/register-page/register-page.component';
import { HomePageComponent } from "./games/home-page/home-page.component";
import { VerifyEmailComponent } from './account/verify-email/verify-email.component';
import { MenuComponent } from "./menu/menu.component";
import { StorePageComponent } from './games/store-page/store-page.component';
import { UpdateProfileComponent } from './profile/update-profile/update-profile.component';
import { ViewProfileComponent } from './profile/view-profile/view-profile.component';
import { InfoGameComponent } from './games/info-game/info-game.component';
import { PurchaseGameComponent } from './games/purchase-game/purchase-game.component';
import { GiveReviewComponent } from './games/give-review/give-review.component';

import { Utils } from "./common/utils";
import { LoginRegisterService } from "./common/services/login-register.service";
import { GameService } from "./common/services/game.service";
import { ProfileService } from "./common/services/profile.service";
import { ReviewService } from "./common/services/review.service";

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from "@angular/forms";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatTableModule } from "@angular/material/table";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MatDividerModule } from "@angular/material/divider";
import { MatDialogModule } from "@angular/material/dialog";
import { MatChipsModule } from "@angular/material/chips";

@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    RegisterPageComponent,
    HomePageComponent,
    VerifyEmailComponent,
    MenuComponent,
    StorePageComponent,
    UpdateProfileComponent,
    ViewProfileComponent,
    InfoGameComponent,
    PurchaseGameComponent,
    GiveReviewComponent,
  ],
  entryComponents: [
    GiveReviewComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatGridListModule,
    MatPaginatorModule,
    MatTableModule,
    MatTooltipModule,
    MatDividerModule,
    MatDialogModule,
    MatChipsModule
  ],
  providers: [
    LoginRegisterService,
    Utils,
    GameService,
    ProfileService,
    ReviewService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
