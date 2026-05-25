import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent {
  get isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }
  constructor(private router: Router) {}
  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}