import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CatsComponent} from "./cats/cats.component";
import {FoodsComponent} from "./foods/foods.component";

const routes: Routes = [
  {path: 'cats', component: CatsComponent},
  {path: 'foods', component: FoodsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
