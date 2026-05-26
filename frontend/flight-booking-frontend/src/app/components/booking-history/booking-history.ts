import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookingService } from '../../services/booking';
import { Router, RouterLink } from '@angular/router';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-booking-history',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './booking-history.html',
  styleUrl: './booking-history.css'
})
export class BookingHistoryComponent implements OnInit {
  bookings: any[] = [];
  loading = false;
  error = '';
  
  // Modale billet sélectionné pour réimpression
  selectedBookingForPrint: any = null;
  showPrintModal = false;

  constructor(private bookingService: BookingService, private router: Router, private toastService: ToastService) {}

  ngOnInit() {
    this.loadHistory();
  }

  loadHistory() {
    this.loading = true;
    this.error = '';
    
    this.bookingService.getMyBookings().subscribe({
      next: (data) => {
        this.bookings = data.sort((a, b) => b.id - a.id) || []; // Les plus récents en premier
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        const token = localStorage.getItem('token');
        if ((err?.status === 401 || err?.status === 403) && token) {
         localStorage.removeItem('token');
         localStorage.removeItem('role');
         this.router.navigate(['/login']);
    } else {
      this.error = "Impossible de charger votre historique de réservations.";
    }
  }
    });
  }

  cancelReservation(id: number) {
    if (confirm("Êtes-vous sûr de vouloir annuler cette réservation ?")) {
      this.bookingService.cancelBooking(id).subscribe({
        next: () => {
          this.toastService.show("Votre réservation a été annulée.", "success");
          this.loadHistory(); // Recharger pour rafraîchir le statut
        },
        error: (err) => {
          this.toastService.show("Erreur lors de l'annulation : " + (err?.error?.message || "Erreur serveur"), "error");
        }
      });
    }
  }

  openBoardingPass(booking: any) {
    this.selectedBookingForPrint = booking;
    this.showPrintModal = true;
  }

  closePrintModal() {
    this.showPrintModal = false;
    this.selectedBookingForPrint = null;
  }

  printTicket() {
    window.print();
  }
}
