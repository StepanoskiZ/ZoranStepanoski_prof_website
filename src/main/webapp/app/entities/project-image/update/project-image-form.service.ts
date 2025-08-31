import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProjectImage, NewProjectImage } from '../project-image.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProjectImage for edit and NewProjectImageFormGroupInput for create.
 */
type ProjectImageFormGroupInput = IProjectImage | PartialWithRequiredKeyOf<NewProjectImage>;

type ProjectImageFormDefaults = Pick<NewProjectImage, 'id'>;

type ProjectImageFormGroupContent = {
  id: FormControl<IProjectImage['id'] | NewProjectImage['id']>;
  imageUrl: FormControl<IProjectImage['imageUrl']>;
  caption: FormControl<IProjectImage['caption']>;
  project: FormControl<IProjectImage['project']>;
};

export type ProjectImageFormGroup = FormGroup<ProjectImageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProjectImageFormService {
  createProjectImageFormGroup(projectImage: ProjectImageFormGroupInput = { id: null }): ProjectImageFormGroup {
    const projectImageRawValue = {
      ...this.getFormDefaults(),
      ...projectImage,
    };
    return new FormGroup<ProjectImageFormGroupContent>({
      id: new FormControl(
        { value: projectImageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      imageUrl: new FormControl(projectImageRawValue.imageUrl, {
        validators: [Validators.required],
      }),
      caption: new FormControl(projectImageRawValue.caption),
      project: new FormControl(projectImageRawValue.project),
    });
  }

  getProjectImage(form: ProjectImageFormGroup): IProjectImage | NewProjectImage {
    return form.getRawValue() as IProjectImage | NewProjectImage;
  }

  resetForm(form: ProjectImageFormGroup, projectImage: ProjectImageFormGroupInput): void {
    const projectImageRawValue = { ...this.getFormDefaults(), ...projectImage };
    form.reset(
      {
        ...projectImageRawValue,
        id: { value: projectImageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProjectImageFormDefaults {
    return {
      id: null,
    };
  }
}
