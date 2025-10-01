import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBusinessService, NewBusinessService } from '../business-service.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBusinessService for edit and NewBusinessServiceFormGroupInput for create.
 */
type BusinessServiceFormGroupInput = IBusinessService | PartialWithRequiredKeyOf<NewBusinessService>;

type BusinessServiceFormDefaults = Pick<NewBusinessService, 'id'>;

type BusinessServiceFormGroupContent = {
  id: FormControl<IBusinessService['id'] | NewBusinessService['id']>;
  title: FormControl<IBusinessService['title']>;
  descriptionHTML: FormControl<IBusinessService['descriptionHTML']>;
  icon: FormControl<IBusinessService['icon']>;
};

export type BusinessServiceFormGroup = FormGroup<BusinessServiceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BusinessServiceFormService {
  createBusinessServiceFormGroup(businessService: BusinessServiceFormGroupInput = { id: null }): BusinessServiceFormGroup {
    const businessServiceRawValue = {
      ...this.getFormDefaults(),
      ...businessService,
    };
    return new FormGroup<BusinessServiceFormGroupContent>({
      id: new FormControl(
        { value: businessServiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(businessServiceRawValue.title, {
        validators: [Validators.required],
      }),
      descriptionHTML: new FormControl(businessServiceRawValue.descriptionHTML, {
        validators: [Validators.required],
      }),
      icon: new FormControl(businessServiceRawValue.icon),
    });
  }

  getBusinessService(form: BusinessServiceFormGroup): IBusinessService | NewBusinessService {
    return form.getRawValue() as IBusinessService | NewBusinessService;
  }

  resetForm(form: BusinessServiceFormGroup, businessService: BusinessServiceFormGroupInput): void {
    const businessServiceRawValue = { ...this.getFormDefaults(), ...businessService };
    form.reset(
      {
        ...businessServiceRawValue,
        id: { value: businessServiceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BusinessServiceFormDefaults {
    return {
      id: null,
    };
  }
}
