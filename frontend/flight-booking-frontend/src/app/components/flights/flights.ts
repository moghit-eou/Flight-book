import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlightService } from '../../services/flight';

@Component({
  selector: 'app-flights',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './flights.html',
  styleUrl: './flights.css'
})
export class FlightsComponent implements OnInit {
  flights: any[] = [];
  loading = true;
  error = '';

  constructor(private flightService: FlightService) {}

  ngOnInit() {
    this.flightService.getFlights('CDG').subscribe({
      next: (data) => {
        this.flights = data.data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des vols';
        this.loading = false;
      }
    });
  }
}