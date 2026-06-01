import { Component, ChangeDetectorRef, AfterViewChecked } from '@angular/core';
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
export class AppComponent implements AfterViewChecked {
  isMobileMenuOpen = false;
  dropdownOpen = false;
  toast: ToastMessage | null = null;
  private toastTimeout: any;

  get isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }
  get isAdmin(): boolean {
    return localStorage.getItem('role') === 'ADMIN';
  }

  constructor(private router: Router, private toastService: ToastService, private cdr: ChangeDetectorRef) {
    this.toastService.toastState.subscribe(toast => {
      this.toast = toast;
      if (this.toastTimeout) clearTimeout(this.toastTimeout);
      this.toastTimeout = setTimeout(() => {
        this.toast = null;
        this.cdr.detectChanges();
      }, 4000);
      setTimeout(() => this.cdr.detectChanges(), 0);
    });
  }

  ngAfterViewChecked() {
    this.cdr.detectChanges();
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('firstName');
    this.router.navigate(['/login']);
  }


  get firstName(): string {
    return localStorage.getItem('firstName') || 'Profil';
  }

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }
}