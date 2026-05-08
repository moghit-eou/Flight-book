import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class FlightService {
  private apiUrl = 'http://localhost:8080/api/flights';

  constructor(private http: HttpClient) {}

  getFlights(depIata?: string, arrIata?: string): Observable<any> {
    let url = this.apiUrl + '?';
    if (depIata) url += `dep_iata=${depIata}&`;
    if (arrIata) url += `arr_iata=${arrIata}`;
    return this.http.get<any>(url);
  }
}