import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { IProjectImage } from '../project-image.model';
import { ProjectImageService } from '../service/project-image.service';
import { ProjectImageFormGroup, ProjectImageFormService } from './project-image-form.service';

@Component({
  selector: 'jhi-project-image-update',
  templateUrl: './project-image-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProjectImageUpdateComponent implements OnInit {
  isSaving = false;
  projectImage: IProjectImage | null = null;

  projectsSharedCollection: IProject[] = [];

  protected projectImageService = inject(ProjectImageService);
  protected projectImageFormService = inject(ProjectImageFormService);
  protected projectService = inject(ProjectService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProjectImageFormGroup = this.projectImageFormService.createProjectImageFormGroup();

  compareProject = (o1: IProject | null, o2: IProject | null): boolean => this.projectService.compareProject(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ projectImage }) => {
      this.projectImage = projectImage;
      if (projectImage) {
        this.updateForm(projectImage);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const projectImage = this.projectImageFormService.getProjectImage(this.editForm);
    if (projectImage.id !== null) {
      this.subscribeToSaveResponse(this.projectImageService.update(projectImage));
    } else {
      this.subscribeToSaveResponse(this.projectImageService.create(projectImage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProjectImage>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(projectImage: IProjectImage): void {
    this.projectImage = projectImage;
    this.projectImageFormService.resetForm(this.editForm, projectImage);

    this.projectsSharedCollection = this.projectService.addProjectToCollectionIfMissing<IProject>(
      this.projectsSharedCollection,
      projectImage.project,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.projectService
      .query()
      .pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
      .pipe(
        map((projects: IProject[]) => this.projectService.addProjectToCollectionIfMissing<IProject>(projects, this.projectImage?.project)),
      )
      .subscribe((projects: IProject[]) => (this.projectsSharedCollection = projects));
  }
}
