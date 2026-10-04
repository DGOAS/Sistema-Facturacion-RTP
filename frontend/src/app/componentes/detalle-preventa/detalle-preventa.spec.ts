import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DetallePreventa } from './detalle-preventa';

describe('DetallePreventa', () => {
  let component: DetallePreventa;
  let fixture: ComponentFixture<DetallePreventa>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetallePreventa],
    }).compileComponents();

    fixture = TestBed.createComponent(DetallePreventa);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
