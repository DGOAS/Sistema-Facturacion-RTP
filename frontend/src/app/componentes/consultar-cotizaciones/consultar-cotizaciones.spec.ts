import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ConsultarCotizaciones } from './consultar-cotizaciones';

describe('ConsultarCotizaciones', () => {
  let component: ConsultarCotizaciones;
  let fixture: ComponentFixture<ConsultarCotizaciones>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsultarCotizaciones],
    }).compileComponents();

    fixture = TestBed.createComponent(ConsultarCotizaciones);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
