import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router'; // 1. Import nécessaire
import { FlightsComponent } from './components/flights/flights';
import { RegisterComponent } from './components/register/register';

@Component({
  selector: 'app-root',
  standalone: true,
  // 2. Ajoute RouterOutlet et RouterLink ici !
  imports: [RouterOutlet, RouterLink, FlightsComponent, RegisterComponent], 
  templateUrl: './app.html',
})
export class AppComponent {
  title = 'flight-booking-frontend';
}