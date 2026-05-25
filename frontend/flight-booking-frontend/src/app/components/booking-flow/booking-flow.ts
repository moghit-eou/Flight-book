import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { BookingService } from '../../services/booking';
import { Router } from '@angular/router';

interface Seat {
  id: string;
  row: number;
  col: string;
  type: 'Premium' | 'Standard';
  occupied: boolean;
  price: number;
}

@Component({
  selector: 'app-booking-flow',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './booking-flow.html',
  styleUrl: './booking-flow.css'
})
export class BookingFlowComponent implements OnInit {
  currentStep = 1;
  flightData: any = null;
  passengerForm!: FormGroup;
  paymentForm!: FormGroup;
  loading = false;
  success = false;
  createdBooking: any = null;

  // Sièges
  seats: Seat[] = [];
  selectedSeat: Seat | null = null;
  occupiedSeatsList = ['1B', '2C', '3A', '5D', '8B', '10C', '12A', '14D'];

  // Add-ons
  priorityBoarding = false;
  travelInsurance = false;

  constructor(
    private fb: FormBuilder,
    private bookingService: BookingService,
    private router: Router
  ) {}

  ngOnInit() {
    this.flightData = this.bookingService.getSelectedFlight();
    
    // Si aucun vol n'est sélectionné (ex: rechargement), rediriger vers les vols
    if (!this.flightData || !this.flightData.flight) {
      this.router.navigate(['/flights']);
      return;
    }

    this.initForms();
    this.generateSeatMap();
  }

  initForms() {
    this.passengerForm = this.fb.group({
      passengerName: ['', [Validators.required, Validators.minLength(3)]],
      passengerEmail: ['', [Validators.required, Validators.email]],
      passengerGender: ['M', Validators.required],
      passengerDob: ['', Validators.required],
      passengerNationality: ['Maroc', Validators.required],
      passengerIdType: ['PASSPORT', Validators.required],
      passengerIdNumber: ['', [Validators.required, Validators.minLength(6)]]
    });

    this.paymentForm = this.fb.group({
      cardName: ['', [Validators.required, Validators.minLength(3)]],
      cardNumber: ['', [Validators.required, Validators.pattern(/^\d{16}$/)]],
      cardExpiry: ['', [Validators.required, Validators.pattern(/^(0[1-9]|1[0-2])\/\d{2}$/)]],
      cardCvv: ['', [Validators.required, Validators.pattern(/^\d{3}$/)]]
    });
  }

  generateSeatMap() {
    const cols = ['A', 'B', 'C', 'D'];
    for (let r = 1; r <= 15; r++) {
      for (const c of cols) {
        const id = `${r}${c}`;
        const type = r <= 4 ? 'Premium' : 'Standard';
        const price = r <= 4 ? 25 : 0;
        const occupied = this.occupiedSeatsList.includes(id);
        
        this.seats.push({
          id,
          row: r,
          col: c,
          type,
          occupied,
          price
        });
      }
    }
  }

  selectSeat(seat: Seat) {
    if (seat.occupied) return;
    this.selectedSeat = seat;
  }

  getAddonsPrice(): number {
    let sum = 0;
    if (this.priorityBoarding) sum += 15;
    if (this.travelInsurance) sum += 20;
    return sum;
  }

  getSeatPriceSupplement(): number {
    return this.selectedSeat ? this.selectedSeat.price : 0;
  }

  getTotalPrice(): number {
    if (!this.flightData) return 0;
    return this.flightData.price + this.getSeatPriceSupplement() + this.getAddonsPrice();
  }

  nextStep() {
    if (this.currentStep === 1 && this.passengerForm.invalid) {
      this.passengerForm.markAllAsTouched();
      return;
    }
    if (this.currentStep === 2 && !this.selectedSeat) {
      alert('Veuillez sélectionner un siège pour continuer.');
      return;
    }
    if (this.currentStep < 4) {
      this.currentStep++;
    }
  }

  prevStep() {
    if (this.currentStep > 1) {
      this.currentStep--;
    }
  }

  confirmPayment() {
    if (this.paymentForm.invalid) {
      this.paymentForm.markAllAsTouched();
      return;
    }

    this.loading = true;

    // Concaténer les options de bagages et add-ons
    let baggageDesc = this.flightData.baggage;
    if (this.priorityBoarding) {
      baggageDesc += ' + Embarquement Prioritaire';
    }

    const bookingPayload = {
      flightNumber: this.flightData.flight.flight?.iata || this.flightData.flight.flight?.number,
      departureIata: this.flightData.flight.departure?.iata,
      arrivalIata: this.flightData.flight.arrival?.iata,
      departureTime: this.flightData.flight.departure?.scheduled,
      arrivalTime: this.flightData.flight.arrival?.scheduled,
      airlineName: this.flightData.flight.airline?.name,
      
      passengerName: this.passengerForm.value.passengerName,
      passengerEmail: this.passengerForm.value.passengerEmail,
      passengerGender: this.passengerForm.value.passengerGender,
      passengerDob: this.passengerForm.value.passengerDob,
      passengerNationality: this.passengerForm.value.passengerNationality,
      passengerIdType: this.passengerForm.value.passengerIdType,
      passengerIdNumber: this.passengerForm.value.passengerIdNumber,
      
      seatNumber: this.selectedSeat?.id || 'Sans siège',
      classType: this.flightData.classType,
      price: this.getTotalPrice(),
      baggageOption: baggageDesc,
      status: 'CONFIRMED'
    };

    this.bookingService.createBooking(bookingPayload).subscribe({
      next: (res) => {
        this.loading = false;
        this.success = true;
        this.createdBooking = res;
        this.currentStep = 5;
        this.triggerConfetti();
      },
      error: (err) => {
        this.loading = false;
        alert("Erreur lors de la réservation : " + (err?.error?.message || "Erreur serveur"));
      }
    });
  }

  triggerConfetti() {
    // Petit effet visuel festif de confettis en CSS
    const duration = 3000;
    const end = Date.now() + duration;

    const interval = setInterval(() => {
      if (Date.now() > end) return clearInterval(interval);
      
      const confetti = document.createElement('div');
      confetti.style.position = 'fixed';
      confetti.style.width = '10px';
      confetti.style.height = '10px';
      confetti.style.backgroundColor = ['#f43f5e', '#3b82f6', '#10b981', '#eab308', '#a855f7'][Math.floor(Math.random() * 5)];
      confetti.style.left = Math.random() * 100 + 'vw';
      confetti.style.top = '-20px';
      confetti.style.zIndex = '9999';
      confetti.style.borderRadius = '50%';
      confetti.style.transform = `rotate(${Math.random() * 360}deg)`;
      
      document.body.appendChild(confetti);

      const animation = confetti.animate([
        { transform: `translateY(0) rotate(0deg)`, opacity: 1 },
        { transform: `translateY(105vh) rotate(${360 + Math.random() * 360}deg)`, opacity: 0 }
      ], {
        duration: 2000 + Math.random() * 1500,
        easing: 'cubic-bezier(0.1, 0.8, 0.3, 1)'
      });

      animation.onfinish = () => confetti.remove();
    }, 50);
  }

  printBoardingPass() {
    window.print();
  }

  navigateToFlights() {
    this.router.navigate(['/flights']);
  }

  navigateToHistory() {
    this.router.navigate(['/my-bookings']);
  }
}
