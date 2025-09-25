import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PageContentMediaDetailComponent } from './page-content-media-detail.component';

describe('PageContentMedia Management Detail Component', () => {
  let comp: PageContentMediaDetailComponent;
  let fixture: ComponentFixture<PageContentMediaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageContentMediaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./page-content-media-detail.component').then(m => m.PageContentMediaDetailComponent),
              resolve: { pageContentMedia: () => of({ id: 5814 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PageContentMediaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PageContentMediaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load pageContentMedia on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PageContentMediaDetailComponent);

      // THEN
      expect(instance.pageContentMedia()).toEqual(expect.objectContaining({ id: 5814 }));
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
