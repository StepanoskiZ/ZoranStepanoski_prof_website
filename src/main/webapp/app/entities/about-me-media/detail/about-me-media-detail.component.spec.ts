import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AboutMeMediaDetailComponent } from './about-me-media-detail.component';

describe('AboutMeMedia Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AboutMeMediaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AboutMeMediaDetailComponent,
              resolve: { aboutMeMedia: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AboutMeMediaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load aboutMeMedia on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AboutMeMediaDetailComponent);

      // THEN
      expect(instance.aboutMeMedia).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
