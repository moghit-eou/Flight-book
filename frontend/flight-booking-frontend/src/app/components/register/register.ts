import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { RouterLink, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, CommonModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class RegisterComponent {
  registerForm: FormGroup;
  showPassword = false;
  loading = false;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get passwordStrength(): number {
    const pw = this.registerForm.get('password')?.value || '';
    let score = 0;
    if (pw.length >= 6) score += 33;
    if (pw.length >= 10) score += 33;
    if (/[A-Z]/.test(pw) && /[0-9]/.test(pw)) score += 34;
    return score;
  }

  get strengthLabel(): string {
    const s = this.passwordStrength;
    if (s <= 33) return 'faible';
    if (s <= 66) return 'moyen';
    return 'fort';
  }

  onSubmit() {
    if (this.registerForm.valid) {
      this.loading = true;
      const { email, password } = this.registerForm.value;
      this.authService.register(email, password).subscribe({
        next: () => {
          this.loading = false;
          this.showToast('Compte créé avec succès ! Bienvenue', 'success', 'registerToast');
          setTimeout(() => this.router.navigate(['/login']), 1500);
        },
        error: (err) => {
          this.loading = false;
          const status = err?.status;
          let msg = "Erreur lors de l'inscription";
          if (status === 409 || err?.error?.message?.toLowerCase().includes('exist') || err?.error?.message?.toLowerCase().includes('use')) {
            msg = 'Cet email est déjà utilisé. Veuillez vous connecter.';
          } else if (err?.error?.message) {
            msg = err.error.message;
          }
          this.showToast(msg, 'error', 'registerToast');
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