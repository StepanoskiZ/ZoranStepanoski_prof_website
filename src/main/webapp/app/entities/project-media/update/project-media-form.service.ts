import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProjectMedia, NewProjectMedia } from '../project-media.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProjectMedia for edit and NewProjectMediaFormGroupInput for create.
 */
type ProjectMediaFormGroupInput = IProjectMedia | PartialWithRequiredKeyOf<NewProjectMedia>;

type ProjectMediaFormDefaults = Pick<NewProjectMedia, 'id'>;

type ProjectMediaFormGroupContent = {
  id: FormControl<IProjectMedia['id'] | NewProjectMedia['id']>;
  mediaUrl: FormControl<IProjectMedia['mediaUrl']>;
  projectMediaType: FormControl<IProjectMedia['projectMediaType']>;
  caption: FormControl<IProjectMedia['caption']>;
  project: FormControl<IProjectMedia['project']>;
};

export type ProjectMediaFormGroup = FormGroup<ProjectMediaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProjectMediaFormService {
  createProjectMediaFormGroup(projectMedia: ProjectMediaFormGroupInput = { id: null }): ProjectMediaFormGroup {
    const projectMediaRawValue = {
      ...this.getFormDefaults(),
      ...projectMedia,
    };
    return new FormGroup<ProjectMediaFormGroupContent>({
      id: new FormControl(
        { value: projectMediaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      mediaUrl: new FormControl(projectMediaRawValue.mediaUrl, {
        validators: [Validators.required],
      }),
      projectMediaType: new FormControl(projectMediaRawValue.projectMediaType, {
        validators: [Validators.required],
      }),
      caption: new FormControl(projectMediaRawValue.caption),
      project: new FormControl(projectMediaRawValue.project),
    });
  }

  getProjectMedia(form: ProjectMediaFormGroup): IProjectMedia | NewProjectMedia {
    return form.getRawValue() as IProjectMedia | NewProjectMedia;
  }

  resetForm(form: ProjectMediaFormGroup, projectMedia: ProjectMediaFormGroupInput): void {
    const projectMediaRawValue = { ...this.getFormDefaults(), ...projectMedia };
    form.reset(
      {
        ...projectMediaRawValue,
        id: { value: projectMediaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProjectMediaFormDefaults {
    return {
      id: null,
    };
  }
}
