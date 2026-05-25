import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ToastService, ToastMessage } from './services/toast.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent {
  isMobileMenuOpen = false;
  toast: ToastMessage | null = null;
  private toastTimeout: any;

  get isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }
  get isAdmin(): boolean {
    return localStorage.getItem('role') === 'ADMIN';
  }

  constructor(private router: Router, private toastService: ToastService) {
    this.toastService.toastState.subscribe(toast => {
      this.toast = toast;
      if (this.toastTimeout) clearTimeout(this.toastTimeout);
      this.toastTimeout = setTimeout(() => this.toast = null, 4000);
    });
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    this.router.navigate(['/login']);
  }
}