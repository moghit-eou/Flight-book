import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FlightService } from '../../services/flight';

@Component({
  selector: 'app-flights',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './flights.html',
  styleUrl: './flights.css',
})
export class FlightsComponent implements OnInit {
  flights: any[] = [];
  loading = false;
  error = '';
  depIata = '';
  arrIata = '';

  constructor(private flightService: FlightService) {}

  ngOnInit() {
    this.search();
  }

  search() {
    this.loading = true;
    this.error = '';
    this.flights = [];

    this.flightService.getFlights(this.depIata, this.arrIata).subscribe({
      next: (data) => {
        this.flights = data?.data || [];
        if (this.flights.length === 0 && data?.error) {
          this.error = data.error.message || 'API error — check your API key';
        }
        this.loading = false;
      },
      error: (err) => {
        if (err?.name === 'TimeoutError') {
          this.error = 'Request timed out — the API is taking too long';
        } else if (err?.status === 0) {
          this.error = 'Cannot reach backend — is it running on :8080?';
        } else {
          this.error = `Error ${err?.status ?? ''}: ${err?.message ?? 'Unknown error'}`;
        }
        this.loading = false;
      },
    });
  }

  getStatusClass(status: string): string {
    return status?.toLowerCase() || 'scheduled';
  }
}