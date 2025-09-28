import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CurriculumVitaeMediaDetailComponent } from './curriculum-vitae-media-detail.component';

describe('CurriculumVitaeMedia Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CurriculumVitaeMediaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CurriculumVitaeMediaDetailComponent,
              resolve: { curriculumVitaeMedia: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CurriculumVitaeMediaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load curriculumVitaeMedia on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CurriculumVitaeMediaDetailComponent);

      // THEN
      expect(instance.curriculumVitaeMedia).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
