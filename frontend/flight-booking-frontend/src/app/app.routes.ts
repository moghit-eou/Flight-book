import { Routes } from '@angular/router';
import { RegisterComponent } from './components/register/register';
import { FlightsComponent } from './components/flights/flights';

export const routes: Routes = [
  { path: 'register', component: RegisterComponent },
  { path: 'flights', component: FlightsComponent },
  { path: '', redirectTo: '/flights', pathMatch: 'full' }
];