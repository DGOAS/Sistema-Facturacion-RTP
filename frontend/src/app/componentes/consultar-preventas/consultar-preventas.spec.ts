import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ConsultarPreventas } from './consultar-preventas';

describe('ConsultarPreventas', () => {
  let component: ConsultarPreventas;
  let fixture: ComponentFixture<ConsultarPreventas>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsultarPreventas],
    }).compileComponents();

    fixture = TestBed.createComponent(ConsultarPreventas);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
