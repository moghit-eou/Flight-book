import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class FlightService {
  private apiUrl = 'http://localhost:8080/api/flights';

  constructor(private http: HttpClient) {}

  getFlights(depIata?: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}?dep_iata=${depIata}`);
  }
}