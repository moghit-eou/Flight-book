import { Component } from '@angular/core';
import { FlightsComponent } from './components/flights/flights';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FlightsComponent],
  templateUrl: './app.html',
})
export class AppComponent {}