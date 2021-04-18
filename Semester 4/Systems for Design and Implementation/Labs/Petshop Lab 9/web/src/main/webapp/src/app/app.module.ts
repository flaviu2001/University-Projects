import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {CatsComponent} from './cats/cats.component';
import {CatService} from "./cats/shared/cats.service";
import {CatListComponent} from './cats/cat-list/cat-list.component';
import {CatDetailComponent} from './cats/cat-detail/cat-detail.component';
import { FoodsComponent } from './foods/foods.component';

@NgModule({
  declarations: [
    AppComponent,
    CatsComponent,
    CatListComponent,
    CatDetailComponent,
    FoodsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [CatService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
