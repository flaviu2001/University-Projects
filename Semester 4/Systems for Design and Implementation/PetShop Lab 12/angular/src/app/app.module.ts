import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {CatsShowComponent} from './cats/cats-show/cats-show.component';
import {CatsAddComponent} from './cats/cats-add/cats-add.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {CatsDeleteComponent} from './cats/cats-delete/cats-delete.component';
import {CatsUpdateComponent} from './cats/cats-update/cats-update.component';
import {FoodsAddComponent} from './foods/foods-add/foods-add.component';
import {FoodsDeleteComponent} from './foods/foods-delete/foods-delete.component';
import {FoodsShowComponent} from './foods/foods-show/foods-show.component';
import {FoodsUpdateComponent} from './foods/foods-update/foods-update.component';
import {CustomerAddComponent} from './customer/customer-add/customer-add.component';
import {CustomerDeleteComponent} from './customer/customer-delete/customer-delete.component';
import {CustomerShowComponent} from './customer/customer-show/customer-show.component';
import {CustomerUpdateComponent} from './customer/customer-update/customer-update.component';
import {SwitcherComponent} from './switcher/switcher.component';
import {PetHousePipePipe} from './customer/customer-show/pet-house-pipe.pipe';
import {ToysShowComponent} from './toys/toys-show/toys-show.component';
import { ToysAddComponent } from './toys/toys-add/toys-add.component';
import { ToysDeleteComponent } from './toys/toys-delete/toys-delete.component';
import { ToysUpdateComponent } from './toys/toys-update/toys-update.component';
import { PurchasesShowComponent } from './purchases/purchases-show/purchases-show.component';
import { PurchasesAddComponent } from './purchases/purchases-add/purchases-add.component';
import { ReportComponent } from './report/report.component';

@NgModule({
  declarations: [
    AppComponent,
    CatsShowComponent,
    CatsAddComponent,
    CatsDeleteComponent,
    CatsUpdateComponent,
    FoodsAddComponent,
    FoodsDeleteComponent,
    FoodsShowComponent,
    FoodsUpdateComponent,
    CustomerAddComponent,
    CustomerDeleteComponent,
    CustomerShowComponent,
    CustomerUpdateComponent,
    SwitcherComponent,
    PetHousePipePipe,
    ToysShowComponent,
    ToysAddComponent,
    ToysDeleteComponent,
    ToysUpdateComponent,
    PurchasesShowComponent,
    PurchasesAddComponent,
    ReportComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
