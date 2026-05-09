import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
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
}