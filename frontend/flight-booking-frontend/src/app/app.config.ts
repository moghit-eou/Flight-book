import { ApplicationConfig, importProvidersFrom } from '@angular/core'; // Importe importProvidersFrom
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms'; // Importe le module standard
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(),
    importProvidersFrom(ReactiveFormsModule) // C'est la syntaxe correcte
  ]
};