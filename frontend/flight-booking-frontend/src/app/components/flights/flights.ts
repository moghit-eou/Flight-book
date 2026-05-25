import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FlightService } from '../../services/flight';
import { BookingService } from '../../services/booking';
import { Router } from '@angular/router';
import { ToastService } from '../../services/toast.service';

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
  depIata = '';
  arrIata = '';
  searched = false;

  // Pagination
  currentPage = 1;
  pageSize = 9;
  paginatedFlights: any[] = [];
  totalPages = 1;

  // Sélection et modale de détails
  selectedFlight: any = null;
  selectedClassType: 'Basic' | 'Standard' | 'Plus' = 'Standard';
  showDetailsModal = false;

  constructor(
    private flightService: FlightService,
    private bookingService: BookingService,
    private router: Router,
    private toastService: ToastService
  ) {}

  ngOnInit() {
    // Affiche les vols en cache au chargement
    this.search();
  }

  search() {
    this.depIata = (this.depIata || '').trim().toUpperCase();
    this.arrIata = (this.arrIata || '').trim().toUpperCase();

    this.loading = true;
    this.flights = [];
    this.paginatedFlights = [];
    this.currentPage = 1;

    this.flightService.getFlights(this.depIata, this.arrIata).subscribe({
      next: (data) => {
        this.flights = data?.data || [];
        if (this.flights.length === 0 && data?.error) {
          this.toastService.show(data.error.message || 'Erreur API', 'error');
        }
        this.updatePagination();
        this.loading = false;
        this.searched = true;
      },
      error: (err) => {
        if (err?.name === 'TimeoutError') {
          this.toastService.show('La recherche est trop longue', 'error');
        } else if (err?.status === 0) {
          this.toastService.show('Impossible de joindre le serveur', 'error');
        } else {
          this.toastService.show(`Erreur ${err?.status ?? ''}: ${err?.message ?? 'Inconnue'}`, 'error');
        }
        this.loading = false;
        this.searched = true;
        this.updatePagination();
      },
    });
  }

  updatePagination() {
    this.totalPages = Math.ceil(this.flights.length / this.pageSize) || 1;
    if (this.currentPage > this.totalPages) this.currentPage = 1;
    const start = (this.currentPage - 1) * this.pageSize;
    this.paginatedFlights = this.flights.slice(start, start + this.pageSize);
  }

  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.updatePagination();
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  prevPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePagination();
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  getStatusClass(status: string): string {
    return status?.toLowerCase() || 'scheduled';
  }

  // Générateur de prix stable basé sur le numéro de vol
  getFlightPrice(flightNum: string): number {
    if (!flightNum) return 420;
    let hash = 0;
    for (let i = 0; i < flightNum.length; i++) {
      hash = flightNum.charCodeAt(i) + ((hash << 5) - hash);
    }
    return 320 + Math.abs(hash % 580);
  }

  openFlightDetails(flight: any) {
    this.selectedFlight = flight;
    this.selectedClassType = 'Standard';
    this.showDetailsModal = true;
  }

  closeFlightDetails() {
    this.showDetailsModal = false;
    this.selectedFlight = null;
  }

  selectClassType(type: 'Basic' | 'Standard' | 'Plus') {
    this.selectedClassType = type;
  }

  getClassPrice(basePrice: number, type: 'Basic' | 'Standard' | 'Plus'): number {
    if (type === 'Basic') return basePrice;
    if (type === 'Standard') return basePrice + 30;
    return basePrice + 70;
  }

  continueBooking() {
    if (!this.selectedFlight) return;
    
    // Vérifier si l'utilisateur est connecté, sinon rediriger vers /login
    const token = localStorage.getItem('token');
    
    const basePrice = this.getFlightPrice(this.selectedFlight.flight?.iata || this.selectedFlight.flight?.number);
    const finalPrice = this.getClassPrice(basePrice, this.selectedClassType);
    
    let baggage = 'Bagage à main inclus';
    let classLabel = 'Economy Basic';
    if (this.selectedClassType === 'Standard') {
      baggage = 'Bagage à main + Bagage Cabine';
      classLabel = 'Economy Standard';
    } else if (this.selectedClassType === 'Plus') {
      baggage = 'Bagage à main + Cabine + En soute 23kg';
      classLabel = 'Economy Plus';
    }

    this.bookingService.setSelectedFlight(
      this.selectedFlight,
      classLabel,
      finalPrice,
      baggage
    );

    if (!token) {
      this.closeFlightDetails();
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/booking-flow' } });
      return;
    }

    this.closeFlightDetails();
    this.router.navigate(['/booking-flow']);
  }
}