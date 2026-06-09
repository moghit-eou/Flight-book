import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookingService } from '../../services/booking';
import { Router, RouterLink } from '@angular/router';
import { ToastService } from '../../services/toast.service';
import { ConfirmService } from '../../services/confirm.service';
import { ChangeDetectorRef } from '@angular/core';
import { ReviewService } from '../../services/review';
import { FormsModule } from '@angular/forms';



@Component({
  selector: 'app-booking-history',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './booking-history.html',
  styleUrl: './booking-history.css'
})
export class BookingHistoryComponent implements OnInit {
  bookings: any[] = [];
  loading = false;
  error = '';
  
  selectedBookingForPrint: any = null;
  showPrintModal = false;

constructor(
  private bookingService: BookingService,
  private router: Router,
  private toastService: ToastService,
  private confirmService: ConfirmService,
  private cdr: ChangeDetectorRef,
  private reviewService: ReviewService

) {}

  ngOnInit() {
    this.loadHistory();
  }

loadHistory() {
  this.loading = true;
  this.error = '';
  this.bookingService.getMyBookings().subscribe({
    next: (data) => {
      this.bookings = data.sort((a, b) => b.id - a.id) || [];
      this.loading = false;
      this.cdr.detectChanges();
    },
    error: (err) => {
      this.loading = false;
      if (err?.status === 401 || err?.status === 403) {
        this.router.navigate(['/login']);
      } else {
        this.error = "Impossible de charger votre historique de réservations.";
      }
      this.cdr.detectChanges();
    }
  });
}

  cancelReservation(id: number) {
    this.confirmService.confirm("Êtes-vous sûr de vouloir annuler cette réservation ?").then(confirmed => {
      if (confirmed) {
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
    });
  }

  openBoardingPass(booking: any) {
    this.selectedBookingForPrint = booking;
    this.showPrintModal = true;
    setTimeout(() => {
      document.querySelector('.modal-overlay')?.classList.add('active');
    }, 0);
  }

    
  closePrintModal() {
    document.querySelector('.modal-overlay')?.classList.remove('active');
    this.showPrintModal = false;
    this.selectedBookingForPrint = null;
  }

  printTicket() {
    window.print();
  }

  setRating(booking: any, star: number) {
     booking.tempRating = star;
  }
submitReview(booking: any) {
      console.log("\n\n====================\n\n");
      console.log('Sending flightIata:', booking.flightNumber);
      console.log('booking object:', booking);   
      console.log('flightId:', booking.flightId);  
      console.log("\n\n====================\n\n");
  if (!booking.tempRating) {
    this.toastService.show("Veuillez sélectionner une note", "error");
    return;
  }
  this.reviewService.addReview(booking.flightNumber, booking.tempRating, booking.tempComment).subscribe({
    next: () => {
      booking.reviewed = true;
      this.toastService.show("Avis envoyé, merci !", "success");
      this.cdr.detectChanges();
    },
    error: () => {
      this.toastService.show("Erreur lors de l'envoi de l'avis", "error");
    }
  });
}

}
