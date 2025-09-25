import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { VisitorLogDetailComponent } from './visitor-log-detail.component';

describe('VisitorLog Management Detail Component', () => {
  let comp: VisitorLogDetailComponent;
  let fixture: ComponentFixture<VisitorLogDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VisitorLogDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./visitor-log-detail.component').then(m => m.VisitorLogDetailComponent),
              resolve: { visitorLog: () => of({ id: 18305 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VisitorLogDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VisitorLogDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load visitorLog on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VisitorLogDetailComponent);

      // THEN
      expect(instance.visitorLog()).toEqual(expect.objectContaining({ id: 18305 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
