import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PageContentMediaDetailComponent } from './page-content-media-detail.component';

describe('PageContentMedia Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageContentMediaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PageContentMediaDetailComponent,
              resolve: { pageContentMedia: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PageContentMediaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load pageContentMedia on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PageContentMediaDetailComponent);

      // THEN
      expect(instance.pageContentMedia).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
