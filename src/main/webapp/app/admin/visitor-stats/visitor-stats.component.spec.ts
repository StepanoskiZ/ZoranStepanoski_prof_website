import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VisitorStatsComponent } from './visitor-stats.component';

describe('VisitorStatsComponent', () => {
  let component: VisitorStatsComponent;
  let fixture: ComponentFixture<VisitorStatsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VisitorStatsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(VisitorStatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
