import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { RegisterComponent } from './components/register/register';
import { FlightsComponent } from './components/flights/flights';
import { BookingFlowComponent } from './components/booking-flow/booking-flow';
import { BookingHistoryComponent } from './components/booking-history/booking-history';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'flights', component: FlightsComponent },
  { path: 'booking-flow', component: BookingFlowComponent },
  { path: 'my-bookings', component: BookingHistoryComponent },
  { path: 'admin/dashboard', component: AdminDashboardComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];