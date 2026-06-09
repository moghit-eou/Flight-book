import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookingService } from '../../services/booking';
import { Router } from '@angular/router';
import { ToastService } from '../../services/toast.service';
import { ConfirmService } from '../../services/confirm.service';
import { ChangeDetectorRef } from '@angular/core';
import { ReviewService } from '../../services/review';
import { FlightService } from '../../services/flight';



@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css'
  
})

export class AdminDashboardComponent implements OnInit {
  stats: any = null;
  bookings: any[] = [];
  loading = false;
  reviews: any[] = [];

  constructor(
    private bookingService: BookingService, 
    private router: Router,
    private toastService: ToastService, 
    private confirmService: ConfirmService,
    private cdr: ChangeDetectorRef,
    private reviewService: ReviewService,
    private flightService: FlightService

  ) {}

  ngOnInit() {
    const role = localStorage.getItem('role');
    if (role !== 'ADMIN') {
      this.router.navigate(['/']);
      return;
    }
    this.loadData();

    this.reviewService.getAllReviews().subscribe({
      next: (data) => {
        this.reviews = data;
        this.cdr.detectChanges();  
      },
      error: () => {}
    });

  }

  loadData() {
    this.loading = true;
    this.bookingService.getAdminStats().subscribe({
      next: (data) => {
        this.stats = data;
        this.bookingService.getAllBookings().subscribe({
          next: (books) => {
            this.bookings = books.sort((a, b) => b.id - a.id);
            this.loading = false;
            this.cdr.detectChanges();  // 
          },
          error: (err) => {
            this.loading = false;
            this.toastService.show("Erreur chargement des réservations", "error");
            this.cdr.detectChanges();  // 

          }
        });
      },
      error: (err) => {
        this.loading = false;
        if (err?.status === 401 || err?.status === 403) {
          this.router.navigate(['/login']);
        } else {
          this.toastService.show("Erreur chargement des statistiques", "error");
        }
        this.cdr.detectChanges();
      }
    });
  }

  cancelReservation(id: number) {
    this.confirmService.confirm("Confirmez-vous l'annulation en tant qu'administrateur ?").then(confirmed => {
      if (confirmed) {
        this.bookingService.cancelBooking(id).subscribe({
          next: () => {
            this.toastService.show("Réservation annulée (Admin).", "success");
            this.loadData();
          },
          error: (err) => {
            this.toastService.show("Erreur annulation admin : " + (err?.error?.message || "Erreur serveur"), "error");
          }
        });
      }
    });
  }

  markAsFlewBooking(id: number) {
    this.confirmService.confirm('Marquer ce vol comme effectué ? Les passagers ne pourront plus annuler.').then(confirmed => {
      if (confirmed) {
        this.bookingService.markAsFlewBooking(id).subscribe({
          next: () => {
            const b = this.bookings.find((x: any) => x.id === id);
            if (b) b.status = 'FLEW';
            this.toastService.show('Vol marqué comme effectué ✈️', 'success');
          },
          error: () => this.toastService.show('Erreur lors de la mise à jour', 'error')
        });
      }
    });
  }

  refreshFlightCache() {
    this.confirmService.confirm("Vider le cache des vols ? La prochaine recherche appellera l'API externe.").then(confirmed => {
      if (confirmed) {
        this.flightService.clearCache().subscribe({
          next: () => this.toastService.show('Cache des vols vidé avec succès ✅', 'success'),
          error: () => this.toastService.show('Erreur lors du vidage du cache', 'error')
        });
      }
    });
  }
}
