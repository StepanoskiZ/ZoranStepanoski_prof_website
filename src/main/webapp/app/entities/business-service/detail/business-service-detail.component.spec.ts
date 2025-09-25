import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { BusinessServiceDetailComponent } from './business-service-detail.component';

describe('BusinessService Management Detail Component', () => {
  let comp: BusinessServiceDetailComponent;
  let fixture: ComponentFixture<BusinessServiceDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BusinessServiceDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./business-service-detail.component').then(m => m.BusinessServiceDetailComponent),
              resolve: { businessService: () => of({ id: 917 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BusinessServiceDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BusinessServiceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load businessService on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BusinessServiceDetailComponent);

      // THEN
      expect(instance.businessService()).toEqual(expect.objectContaining({ id: 917 }));
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
