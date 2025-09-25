import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { ProjectMediaService } from '../service/project-media.service';
import { IProjectMedia } from '../project-media.model';
import { ProjectMediaFormService } from './project-media-form.service';

import { ProjectMediaUpdateComponent } from './project-media-update.component';

describe('ProjectMedia Management Update Component', () => {
  let comp: ProjectMediaUpdateComponent;
  let fixture: ComponentFixture<ProjectMediaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let projectMediaFormService: ProjectMediaFormService;
  let projectMediaService: ProjectMediaService;
  let projectService: ProjectService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProjectMediaUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProjectMediaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProjectMediaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    projectMediaFormService = TestBed.inject(ProjectMediaFormService);
    projectMediaService = TestBed.inject(ProjectMediaService);
    projectService = TestBed.inject(ProjectService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Project query and add missing value', () => {
      const projectMedia: IProjectMedia = { id: 8913 };
      const project: IProject = { id: 10300 };
      projectMedia.project = project;

      const projectCollection: IProject[] = [{ id: 10300 }];
      jest.spyOn(projectService, 'query').mockReturnValue(of(new HttpResponse({ body: projectCollection })));
      const additionalProjects = [project];
      const expectedCollection: IProject[] = [...additionalProjects, ...projectCollection];
      jest.spyOn(projectService, 'addProjectToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ projectMedia });
      comp.ngOnInit();

      expect(projectService.query).toHaveBeenCalled();
      expect(projectService.addProjectToCollectionIfMissing).toHaveBeenCalledWith(
        projectCollection,
        ...additionalProjects.map(expect.objectContaining),
      );
      expect(comp.projectsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const projectMedia: IProjectMedia = { id: 8913 };
      const project: IProject = { id: 10300 };
      projectMedia.project = project;

      activatedRoute.data = of({ projectMedia });
      comp.ngOnInit();

      expect(comp.projectsSharedCollection).toContainEqual(project);
      expect(comp.projectMedia).toEqual(projectMedia);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProjectMedia>>();
      const projectMedia = { id: 4307 };
      jest.spyOn(projectMediaFormService, 'getProjectMedia').mockReturnValue(projectMedia);
      jest.spyOn(projectMediaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ projectMedia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: projectMedia }));
      saveSubject.complete();

      // THEN
      expect(projectMediaFormService.getProjectMedia).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(projectMediaService.update).toHaveBeenCalledWith(expect.objectContaining(projectMedia));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProjectMedia>>();
      const projectMedia = { id: 4307 };
      jest.spyOn(projectMediaFormService, 'getProjectMedia').mockReturnValue({ id: null });
      jest.spyOn(projectMediaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ projectMedia: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: projectMedia }));
      saveSubject.complete();

      // THEN
      expect(projectMediaFormService.getProjectMedia).toHaveBeenCalled();
      expect(projectMediaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProjectMedia>>();
      const projectMedia = { id: 4307 };
      jest.spyOn(projectMediaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ projectMedia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(projectMediaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProject', () => {
      it('should forward to projectService', () => {
        const entity = { id: 10300 };
        const entity2 = { id: 3319 };
        jest.spyOn(projectService, 'compareProject');
        comp.compareProject(entity, entity2);
        expect(projectService.compareProject).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
