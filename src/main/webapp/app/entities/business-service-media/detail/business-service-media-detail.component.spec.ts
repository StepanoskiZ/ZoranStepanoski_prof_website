import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BusinessServiceMediaDetailComponent } from './business-service-media-detail.component';

describe('BusinessServiceMedia Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BusinessServiceMediaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: BusinessServiceMediaDetailComponent,
              resolve: { businessServiceMedia: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BusinessServiceMediaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load businessServiceMedia on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BusinessServiceMediaDetailComponent);

      // THEN
      expect(instance.businessServiceMedia).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
