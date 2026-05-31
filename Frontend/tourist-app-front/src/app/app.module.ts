import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegisterComponent } from './pages/register/register.component';
import { LoginComponent } from './pages/login/login.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { NavbarComponent } from './navbar/navbar.component';
import { AdminComponent } from './pages/admin/admin.component';
import { FeedComponentComponent } from './feed-component/feed-component.component';
import { BlogComponent } from './blog/blog.component';
import { ReviewFormComponent } from './pages/review-form/review-form.component';
import { MyToursComponent } from './pages/my-tours/my-tours.component';
import { ToursComponent } from './pages/tours/tours.component';
import { ShoppingCartComponent } from './pages/shopping-cart/shopping-cart.component';
import { ExploreToursComponent } from './pages/explore-tours/explore-tours.component';

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    ProfileComponent,
    NavbarComponent,
    AdminComponent,
    FeedComponentComponent,
    BlogComponent,
    ReviewFormComponent,
    MyToursComponent,
    ToursComponent,
    ShoppingCartComponent,
    ExploreToursComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
