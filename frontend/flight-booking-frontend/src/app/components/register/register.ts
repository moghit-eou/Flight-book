import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { RouterLink, Router, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../services/toast.service';

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

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router, private route: ActivatedRoute, private toastService: ToastService) {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      city: ['', Validators.required],
      country: ['', Validators.required],
      phoneNumber: ['', Validators.required]
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
      const { email, password, confirmPassword, firstName, lastName, city, country, phoneNumber } = this.registerForm.value;
      
      if (password !== confirmPassword) {
        this.toastService.show('Les mots de passe ne correspondent pas', 'error');
        this.loading = false;
        return;
     }

      this.authService.register(email, password, firstName, lastName, city, country, phoneNumber).subscribe({
        next: (res) => {
          this.loading = false;
          this.toastService.show('Compte créé avec succès ! Bienvenue', 'success');
          localStorage.setItem('firstName', res.firstName);
          setTimeout(() => {
            const returnUrl = this.route.snapshot.queryParams['returnUrl'];
            if (returnUrl) {
              this.router.navigate(['/login'], { queryParams: { returnUrl } });
            } else {
              this.router.navigate(['/login']);
            }
          }, 1500);
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
          this.toastService.show(msg, 'error');
        }
      });
    }
  }

  debugForm() {
    console.log(this.registerForm.value);
    Object.keys(this.registerForm.controls).forEach(key => {
      console.log(key, this.registerForm.get(key)?.errors);
    });
  }
}