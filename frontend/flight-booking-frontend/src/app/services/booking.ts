import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class BookingService {
  private apiUrl = 'http://localhost:8080/api/bookings';

  // Stockage temporaire du vol sélectionné pour le processus de réservation
  private selectedFlight: any = null;
  private selectedClassPrice: number = 0;
  private selectedClassType: string = 'Economy Standard';
  private baggageOption: string = 'Personal + Carry-on';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  setSelectedFlight(flight: any, classType: string, price: number, baggage: string) {
    this.selectedFlight = flight;
    this.selectedClassType = classType;
    this.selectedClassPrice = price;
    this.baggageOption = baggage;

    // Persist in sessionStorage to survive page reloads
    try {
      sessionStorage.setItem('selectedFlight', JSON.stringify(flight));
      sessionStorage.setItem('selectedClassType', classType);
      sessionStorage.setItem('selectedClassPrice', price.toString());
      sessionStorage.setItem('baggageOption', baggage);
    } catch (e) {}
  }

  getSelectedFlight() {
    if (!this.selectedFlight) {
      try {
        const storedFlight = sessionStorage.getItem('selectedFlight');
        if (storedFlight) {
          this.selectedFlight = JSON.parse(storedFlight);
          this.selectedClassType = sessionStorage.getItem('selectedClassType') || 'Economy Standard';
          this.selectedClassPrice = Number(sessionStorage.getItem('selectedClassPrice') || 0);
          this.baggageOption = sessionStorage.getItem('baggageOption') || 'Personal + Carry-on';
        }
      } catch (e) {}
    }

    return {
      flight: this.selectedFlight,
      classType: this.selectedClassType,
      price: this.selectedClassPrice,
      baggage: this.baggageOption
    };
  }

  createBooking(bookingData: any): Observable<any> {
    return this.http.post(this.apiUrl, bookingData, { headers: this.getHeaders() });
  }

  getMyBookings(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/my-bookings`, { headers: this.getHeaders() });
  }

  getAllBookings(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/admin/all`, { headers: this.getHeaders() });
  }

  getAdminStats(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/admin/stats`, { headers: this.getHeaders() });
  }

  cancelBooking(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }
}
