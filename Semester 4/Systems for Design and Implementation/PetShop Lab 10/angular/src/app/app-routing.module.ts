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
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
