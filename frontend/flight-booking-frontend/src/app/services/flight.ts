import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';
import { timeout } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class FlightService {
  private apiUrl = 'http://localhost:8080/api/flights';

  constructor(private http: HttpClient) {}

  getFlights(depIata?: string, arrIata?: string): Observable<any> {
    let params = new HttpParams();
    if (depIata?.trim()) params = params.set('dep_iata', depIata.trim());
    if (arrIata?.trim()) params = params.set('arr_iata', arrIata.trim());

    return this.http.get<any>(this.apiUrl, { params }).pipe(timeout(15000));
  }


  clearCache(): Observable<any> {
    const token = localStorage.getItem('token') || '';
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.delete<any>(`${this.apiUrl}/cache`, { headers });
  }
}