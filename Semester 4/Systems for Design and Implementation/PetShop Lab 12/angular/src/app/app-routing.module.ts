import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CatsShowComponent} from "./cats/cats-show/cats-show.component";
import {CatsAddComponent} from "./cats/cats-add/cats-add.component";
import {CatsDeleteComponent} from "./cats/cats-delete/cats-delete.component";
import {CatsUpdateComponent} from "./cats/cats-update/cats-update.component";
import {FoodsShowComponent} from "./foods/foods-show/foods-show.component";
import {FoodsAddComponent} from "./foods/foods-add/foods-add.component";
import {FoodsDeleteComponent} from "./foods/foods-delete/foods-delete.component";
import {FoodsUpdateComponent} from "./foods/foods-update/foods-update.component";
import {CustomerShowComponent} from "./customer/customer-show/customer-show.component";
import {CustomerAddComponent} from "./customer/customer-add/customer-add.component";
import {CustomerDeleteComponent} from "./customer/customer-delete/customer-delete.component";
import {CustomerUpdateComponent} from "./customer/customer-update/customer-update.component";
import {SwitcherComponent} from "./switcher/switcher.component";
import {ToysShowComponent} from "./toys/toys-show/toys-show.component";
import {ToysAddComponent} from "./toys/toys-add/toys-add.component";
import {ToysDeleteComponent} from "./toys/toys-delete/toys-delete.component";
import {ToysUpdateComponent} from "./toys/toys-update/toys-update.component";
import {PurchasesShowComponent} from "./purchases/purchases-show/purchases-show.component";
import {PurchasesAddComponent} from "./purchases/purchases-add/purchases-add.component";
import {ReportComponent} from "./report/report.component";

const routes: Routes = [
  {path: '', redirectTo: 'switcher', pathMatch: 'full'},
  {path: 'switcher', component: SwitcherComponent},
  {path: 'showCats', component: CatsShowComponent},
  {path: 'addCat', component: CatsAddComponent},
  {path: 'deleteCat', component: CatsDeleteComponent},
  {path: 'updateCat', component: CatsUpdateComponent},
  {path: 'showFoods', component: FoodsShowComponent},
  {path: 'addFood', component: FoodsAddComponent},
  {path: 'deleteFood', component: FoodsDeleteComponent},
  {path: 'updateFood', component: FoodsUpdateComponent},
  {path: 'showCustomers', component: CustomerShowComponent},
  {path: 'addCustomer', component: CustomerAddComponent},
  {path: 'deleteCustomer', component: CustomerDeleteComponent},
  {path: 'updateCustomer', component: CustomerUpdateComponent},
  {path: 'showToys', component: ToysShowComponent},
  {path: 'addToy', component: ToysAddComponent},
  {path: 'deleteToy', component: ToysDeleteComponent},
  {path: 'updateToy', component: ToysUpdateComponent},
  {path: 'showPurchases', component: PurchasesShowComponent},
  {path: 'addPurchase', component: PurchasesAddComponent},
  {path: 'report', component: ReportComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
