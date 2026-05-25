import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { RegisterComponent } from './components/register/register';
import { FlightsComponent } from './components/flights/flights';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'flights', component: FlightsComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];