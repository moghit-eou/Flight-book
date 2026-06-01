import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookingService } from '../../services/booking';
import { Router } from '@angular/router';
import { ToastService } from '../../services/toast.service';
import { ChangeDetectorRef } from '@angular/core';


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
  
  constructor(
    private bookingService: BookingService, 
    private router: Router,
    private toastService: ToastService, 
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    const role = localStorage.getItem('role');
    if (role !== 'ADMIN') {
      this.router.navigate(['/']);
      return;
    }
    this.loadData();
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
    if (confirm("Confirmez-vous l'annulation en tant qu'administrateur ?")) {
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
  }

  markAsFlewBooking(id: number) {
  if (!confirm('Marquer ce vol comme effectué ? Les passagers ne pourront plus annuler.')) return;
  this.bookingService.markAsFlewBooking(id).subscribe({
    next: () => {
      const b = this.bookings.find((x: any) => x.id === id);
      if (b) b.status = 'FLEW';
      this.toastService.show('Vol marqué comme effectué ✈️', 'success');
    },
    error: () => this.toastService.show('Erreur lors de la mise à jour', 'error')
  });
}
}
