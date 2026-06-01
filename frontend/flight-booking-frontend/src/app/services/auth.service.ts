import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  register(email: string, password: string, firstName: string, lastName: string, city: string, country: string, phoneNumber: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, { email, password, firstName, lastName, city, country, phoneNumber });
  }

  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { email, password });
  }
}