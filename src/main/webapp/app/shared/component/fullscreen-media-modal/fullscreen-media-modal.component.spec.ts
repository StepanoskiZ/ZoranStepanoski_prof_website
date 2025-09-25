import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FullscreenMediaModalComponent } from './fullscreen-media-modal.component';

describe('FullscreenMediaModalComponent', () => {
  let component: FullscreenMediaModalComponent;
  let fixture: ComponentFixture<FullscreenMediaModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FullscreenMediaModalComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FullscreenMediaModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
