import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ReviewService {
  private apiUrl = 'http://localhost:8080/api/reviews';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  addReview(flightIata: string, rating: number, comment: string): Observable<any> {
    return this.http.post(this.apiUrl, { flightIata, rating, comment }, { headers: this.getHeaders() });
  }

  getByFlight(flightId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/flight/${flightId}`);
  }

  getMyReviews(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/my-reviews`, { headers: this.getHeaders() });
  }
  
  getAllReviews(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/admin/all`, { headers: this.getHeaders() });
  }

}