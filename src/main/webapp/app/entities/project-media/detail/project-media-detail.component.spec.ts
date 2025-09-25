import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProjectMediaDetailComponent } from './project-media-detail.component';

describe('ProjectMedia Management Detail Component', () => {
  let comp: ProjectMediaDetailComponent;
  let fixture: ComponentFixture<ProjectMediaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProjectMediaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./project-media-detail.component').then(m => m.ProjectMediaDetailComponent),
              resolve: { projectMedia: () => of({ id: 4307 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProjectMediaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectMediaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load projectMedia on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProjectMediaDetailComponent);

      // THEN
      expect(instance.projectMedia()).toEqual(expect.objectContaining({ id: 4307 }));
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
