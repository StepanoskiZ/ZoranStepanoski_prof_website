import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { VisitorLogDetailComponent } from './visitor-log-detail.component';

describe('VisitorLog Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VisitorLogDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: VisitorLogDetailComponent,
              resolve: { visitorLog: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VisitorLogDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load visitorLog on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VisitorLogDetailComponent);

      // THEN
      expect(instance.visitorLog).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
