import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FlightService } from '../../services/flight';

@Component({
  selector: 'app-flights',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './flights.html',
  styleUrl: './flights.css'
})
export class FlightsComponent implements OnInit {
  flights: any[] = [];
  loading = false;
  error = '';
  depIata = 'CDG';
  arrIata = '';

  constructor(private flightService: FlightService) {}

  ngOnInit() { this.search(); }

  search() {
    this.loading = true;
    this.error = '';
    this.flights = [];
    this.flightService.getFlights(this.depIata, this.arrIata).subscribe({
      next: (data) => {
        this.flights = data.data || [];
        this.loading = false;
      },
      error: () => {
        this.error = 'Erreur lors du chargement des vols';
        this.loading = false;
      }
    });
  }

  getStatusClass(status: string): string {
    return status?.toLowerCase() || 'scheduled';
  }
}