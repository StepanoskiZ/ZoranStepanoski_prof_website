import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurriculumVitaeDetailModalComponent } from './curriculum-vitae-detail-modal.component';

describe('CurriculumVitaeDetailModalComponent', () => {
  let component: CurriculumVitaeDetailModalComponent;
  let fixture: ComponentFixture<CurriculumVitaeDetailModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CurriculumVitaeDetailModalComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(CurriculumVitaeDetailModalComponent);
    component = fixture.componentInstance;
    // You might need to provide a mock 'item' input for the test to run
    component.item = { id: 1, companyName: 'Test Company' };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
