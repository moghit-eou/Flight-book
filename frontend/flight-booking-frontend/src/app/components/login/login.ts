import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  showPassword = false;
  loading = false;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.loading = true;
      this.authService.login(this.loginForm.value.email, this.loginForm.value.password).subscribe({
        next: (res) => {
          localStorage.setItem('token', res.token);
          this.showToast('Connexion réussie ! Bienvenue', 'success', 'loginToast');
          setTimeout(() => this.router.navigate(['/flights']), 900);
        },
        error: (err) => {
          this.loading = false;
          const msg = err?.error?.message || 'Email ou mot de passe incorrect';
          this.showToast(msg, 'error', 'loginToast');
        }
      });
    }
  }

  showToast(message: string, type: 'success' | 'error', containerId: string) {
    const container = document.getElementById(containerId);
    if (!container) return;
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `<i class="ti ${type === 'success' ? 'ti-circle-check' : 'ti-alert-circle'}"></i><span>${message}</span>`;
    container.appendChild(toast);
    setTimeout(() => toast.classList.add('toast-show'), 10);
    setTimeout(() => {
      toast.classList.remove('toast-show');
      setTimeout(() => toast.remove(), 300);
    }, 4000);
  }
}