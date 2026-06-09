import { Component, ChangeDetectorRef, AfterViewChecked } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ToastService, ToastMessage } from './services/toast.service';
import { ConfirmService } from './services/confirm.service';

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

  showConfirmModal = false;
  confirmMessage = '';
  private pendingResolve: ((value: boolean) => void) | null = null;

  constructor(
    private router: Router, 
    private toastService: ToastService, 
    private confirmService: ConfirmService,
    private cdr: ChangeDetectorRef
  ) {
    this.toastService.toastState.subscribe(toast => {
      this.toast = toast;
      if (this.toastTimeout) clearTimeout(this.toastTimeout);
      this.toastTimeout = setTimeout(() => {
        this.toast = null;
        this.cdr.detectChanges();
      }, 4000);
      setTimeout(() => this.cdr.detectChanges(), 0);
    });

    this.confirmService.confirmState.subscribe(request => {
      this.confirmMessage = request.message;
      this.pendingResolve = request.resolve;
      this.showConfirmModal = true;
      this.cdr.detectChanges();
    });
  }

  handleConfirmResponse(response: boolean) {
    if (this.pendingResolve) {
      this.pendingResolve(response);
      this.pendingResolve = null;
    }
    this.showConfirmModal = false;
    this.cdr.detectChanges();
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