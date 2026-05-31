import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './pages/register/register.component';
import { LoginComponent } from './pages/login/login.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { AdminComponent } from './pages/admin/admin.component';
import { FeedComponentComponent } from './feed-component/feed-component.component';
import { BlogComponent } from './blog/blog.component';
import { MyToursComponent } from 'src/app/pages/my-tours/my-tours.component';
import { ReviewFormComponent } from './pages/review-form/review-form.component';
import { PositionSimulatorComponent } from './pages/position-simulator/position-simulator.component';
import { ToursComponent } from 'src/app/pages/tours/tours.component';
import { ShoppingCartComponent } from './pages/shopping-cart/shopping-cart.component';
import { ExploreToursComponent } from './pages/explore-tours/explore-tours.component';

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'admin', component: AdminComponent },
  { path: 'feed', component: FeedComponentComponent },
  { path: 'blog/:id', component: BlogComponent },
  { path: 'my-tours', component: MyToursComponent },
  { path: 'review/:tourId', component: ReviewFormComponent },
  { path: 'position-simulator', component: PositionSimulatorComponent },
  { path: 'tours', component: ToursComponent },
  { path: 'cart', component: ShoppingCartComponent },
  { path: 'explore-tours', component: ExploreToursComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
