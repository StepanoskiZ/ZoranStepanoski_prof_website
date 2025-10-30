import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VisitorLogMapComponent } from './visitor-log-map.component';

describe('VisitorLogMapComponent', () => {
  let component: VisitorLogMapComponent;
  let fixture: ComponentFixture<VisitorLogMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VisitorLogMapComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(VisitorLogMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
