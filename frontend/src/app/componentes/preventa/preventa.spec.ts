import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Preventa } from './preventa';

describe('Preventa', () => {
  let component: Preventa;
  let fixture: ComponentFixture<Preventa>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Preventa],
    }).compileComponents();

    fixture = TestBed.createComponent(Preventa);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
