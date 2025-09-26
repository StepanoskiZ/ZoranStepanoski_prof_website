import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { UnifiedMediaType } from 'app/entities/enumerations/unified-media-type.model';
import { ProjectMediaService } from '../service/project-media.service';
import { IProjectMedia } from '../project-media.model';
import { ProjectMediaFormService, ProjectMediaFormGroup } from './project-media-form.service';

@Component({
  selector: 'jhi-project-media-update',
  standalone: true,
  templateUrl: './project-media-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProjectMediaUpdateComponent implements OnInit {
  isSaving = false;
  projectMedia: IProjectMedia | null = null;
  unifiedMediaTypeValues = Object.keys(UnifiedMediaType);

  projectsSharedCollection: IProject[] = [];

  editForm: ProjectMediaFormGroup = this.projectMediaFormService.createProjectMediaFormGroup();

  constructor(
    protected projectMediaService: ProjectMediaService,
    protected projectMediaFormService: ProjectMediaFormService,
    protected projectService: ProjectService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareProject = (o1: IProject | null, o2: IProject | null): boolean => this.projectService.compareProject(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ projectMedia }) => {
      this.projectMedia = projectMedia;
      if (projectMedia) {
        this.updateForm(projectMedia);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const projectMedia = this.projectMediaFormService.getProjectMedia(this.editForm);
    if (projectMedia.id !== null) {
      this.subscribeToSaveResponse(this.projectMediaService.update(projectMedia));
    } else {
      this.subscribeToSaveResponse(this.projectMediaService.create(projectMedia));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProjectMedia>>): void {
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

  protected updateForm(projectMedia: IProjectMedia): void {
    this.projectMedia = projectMedia;
    this.projectMediaFormService.resetForm(this.editForm, projectMedia);

    this.projectsSharedCollection = this.projectService.addProjectToCollectionIfMissing<IProject>(
      this.projectsSharedCollection,
      projectMedia.project,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.projectService
      .query()
      .pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
      .pipe(
        map((projects: IProject[]) => this.projectService.addProjectToCollectionIfMissing<IProject>(projects, this.projectMedia?.project)),
      )
      .subscribe((projects: IProject[]) => (this.projectsSharedCollection = projects));
  }
}
