import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICurriculumVitae, NewCurriculumVitae } from '../curriculum-vitae.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICurriculumVitae for edit and NewCurriculumVitaeFormGroupInput for create.
 */
type CurriculumVitaeFormGroupInput = ICurriculumVitae | PartialWithRequiredKeyOf<NewCurriculumVitae>;

type CurriculumVitaeFormDefaults = Pick<NewCurriculumVitae, 'id'>;

type CurriculumVitaeFormGroupContent = {
  id: FormControl<ICurriculumVitae['id'] | NewCurriculumVitae['id']>;
  companyName: FormControl<ICurriculumVitae['companyName']>;
  language: FormControl<ICurriculumVitae['language']>;
  jobDescriptionHTML: FormControl<ICurriculumVitae['jobDescriptionHTML']>;
  startDate: FormControl<ICurriculumVitae['startDate']>;
  endDate: FormControl<ICurriculumVitae['endDate']>;
  status: FormControl<ICurriculumVitae['status']>;
  type: FormControl<ICurriculumVitae['type']>;
  category: FormControl<ICurriculumVitae['category']>;
};

export type CurriculumVitaeFormGroup = FormGroup<CurriculumVitaeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CurriculumVitaeFormService {
  createCurriculumVitaeFormGroup(curriculumVitae: CurriculumVitaeFormGroupInput = { id: null }): CurriculumVitaeFormGroup {
    const curriculumVitaeRawValue = {
      ...this.getFormDefaults(),
      ...curriculumVitae,
    };
    return new FormGroup<CurriculumVitaeFormGroupContent>({
      id: new FormControl(
        { value: curriculumVitaeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      companyName: new FormControl(curriculumVitaeRawValue.companyName, {
        validators: [Validators.required],
      }),
      language: new FormControl(curriculumVitaeRawValue.language, {
        validators: [Validators.required],
      }),
      jobDescriptionHTML: new FormControl(curriculumVitaeRawValue.jobDescriptionHTML, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(curriculumVitaeRawValue.startDate),
      endDate: new FormControl(curriculumVitaeRawValue.endDate),
      status: new FormControl(curriculumVitaeRawValue.status),
      type: new FormControl(curriculumVitaeRawValue.type),
      category: new FormControl(curriculumVitaeRawValue.category),
    });
  }

  getCurriculumVitae(form: CurriculumVitaeFormGroup): ICurriculumVitae | NewCurriculumVitae {
    return form.getRawValue() as ICurriculumVitae | NewCurriculumVitae;
  }

  resetForm(form: CurriculumVitaeFormGroup, curriculumVitae: CurriculumVitaeFormGroupInput): void {
    const curriculumVitaeRawValue = { ...this.getFormDefaults(), ...curriculumVitae };
    form.reset(
      {
        ...curriculumVitaeRawValue,
        id: { value: curriculumVitaeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CurriculumVitaeFormDefaults {
    return {
      id: null,
    };
  }
}
