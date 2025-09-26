import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProjectMediaDetailComponent } from './project-media-detail.component';

describe('ProjectMedia Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProjectMediaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ProjectMediaDetailComponent,
              resolve: { projectMedia: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProjectMediaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load projectMedia on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProjectMediaDetailComponent);

      // THEN
      expect(instance.projectMedia).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
