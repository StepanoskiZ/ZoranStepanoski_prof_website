import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { ProjectImageService } from '../service/project-image.service';
import { IProjectImage } from '../project-image.model';
import { ProjectImageFormService } from './project-image-form.service';

import { ProjectImageUpdateComponent } from './project-image-update.component';

describe('ProjectImage Management Update Component', () => {
  let comp: ProjectImageUpdateComponent;
  let fixture: ComponentFixture<ProjectImageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let projectImageFormService: ProjectImageFormService;
  let projectImageService: ProjectImageService;
  let projectService: ProjectService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProjectImageUpdateComponent],
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
      .overrideTemplate(ProjectImageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProjectImageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    projectImageFormService = TestBed.inject(ProjectImageFormService);
    projectImageService = TestBed.inject(ProjectImageService);
    projectService = TestBed.inject(ProjectService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Project query and add missing value', () => {
      const projectImage: IProjectImage = { id: 22654 };
      const project: IProject = { id: 10300 };
      projectImage.project = project;

      const projectCollection: IProject[] = [{ id: 10300 }];
      jest.spyOn(projectService, 'query').mockReturnValue(of(new HttpResponse({ body: projectCollection })));
      const additionalProjects = [project];
      const expectedCollection: IProject[] = [...additionalProjects, ...projectCollection];
      jest.spyOn(projectService, 'addProjectToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ projectImage });
      comp.ngOnInit();

      expect(projectService.query).toHaveBeenCalled();
      expect(projectService.addProjectToCollectionIfMissing).toHaveBeenCalledWith(
        projectCollection,
        ...additionalProjects.map(expect.objectContaining),
      );
      expect(comp.projectsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const projectImage: IProjectImage = { id: 22654 };
      const project: IProject = { id: 10300 };
      projectImage.project = project;

      activatedRoute.data = of({ projectImage });
      comp.ngOnInit();

      expect(comp.projectsSharedCollection).toContainEqual(project);
      expect(comp.projectImage).toEqual(projectImage);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProjectImage>>();
      const projectImage = { id: 30515 };
      jest.spyOn(projectImageFormService, 'getProjectImage').mockReturnValue(projectImage);
      jest.spyOn(projectImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ projectImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: projectImage }));
      saveSubject.complete();

      // THEN
      expect(projectImageFormService.getProjectImage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(projectImageService.update).toHaveBeenCalledWith(expect.objectContaining(projectImage));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProjectImage>>();
      const projectImage = { id: 30515 };
      jest.spyOn(projectImageFormService, 'getProjectImage').mockReturnValue({ id: null });
      jest.spyOn(projectImageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ projectImage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: projectImage }));
      saveSubject.complete();

      // THEN
      expect(projectImageFormService.getProjectImage).toHaveBeenCalled();
      expect(projectImageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProjectImage>>();
      const projectImage = { id: 30515 };
      jest.spyOn(projectImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ projectImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(projectImageService.update).toHaveBeenCalled();
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
