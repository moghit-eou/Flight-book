import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register'; // Le bon nom de classe

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>; // Ici aussi

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterComponent], // Et ici
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent); // Et là
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});