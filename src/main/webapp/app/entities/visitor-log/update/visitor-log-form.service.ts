import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVisitorLog, NewVisitorLog } from '../visitor-log.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVisitorLog for edit and NewVisitorLogFormGroupInput for create.
 */
type VisitorLogFormGroupInput = IVisitorLog | PartialWithRequiredKeyOf<NewVisitorLog>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IVisitorLog | NewVisitorLog> = Omit<T, 'visitTimestamp'> & {
  visitTimestamp?: string | null;
};

type VisitorLogFormRawValue = FormValueOf<IVisitorLog>;

type NewVisitorLogFormRawValue = FormValueOf<NewVisitorLog>;

type VisitorLogFormDefaults = Pick<NewVisitorLog, 'id' | 'visitTimestamp'>;

type VisitorLogFormGroupContent = {
  id: FormControl<VisitorLogFormRawValue['id'] | NewVisitorLog['id']>;
  ipAddress: FormControl<VisitorLogFormRawValue['ipAddress']>;
  pageVisited: FormControl<VisitorLogFormRawValue['pageVisited']>;
  userAgent: FormControl<VisitorLogFormRawValue['userAgent']>;
  visitTimestamp: FormControl<VisitorLogFormRawValue['visitTimestamp']>;
};

export type VisitorLogFormGroup = FormGroup<VisitorLogFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VisitorLogFormService {
  createVisitorLogFormGroup(visitorLog: VisitorLogFormGroupInput = { id: null }): VisitorLogFormGroup {
    const visitorLogRawValue = this.convertVisitorLogToVisitorLogRawValue({
      ...this.getFormDefaults(),
      ...visitorLog,
    });
    return new FormGroup<VisitorLogFormGroupContent>({
      id: new FormControl(
        { value: visitorLogRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      ipAddress: new FormControl(visitorLogRawValue.ipAddress),
      pageVisited: new FormControl(visitorLogRawValue.pageVisited),
      userAgent: new FormControl(visitorLogRawValue.userAgent),
      visitTimestamp: new FormControl(visitorLogRawValue.visitTimestamp, {
        validators: [Validators.required],
      }),
    });
  }

  getVisitorLog(form: VisitorLogFormGroup): IVisitorLog | NewVisitorLog {
    return this.convertVisitorLogRawValueToVisitorLog(form.getRawValue() as VisitorLogFormRawValue | NewVisitorLogFormRawValue);
  }

  resetForm(form: VisitorLogFormGroup, visitorLog: VisitorLogFormGroupInput): void {
    const visitorLogRawValue = this.convertVisitorLogToVisitorLogRawValue({ ...this.getFormDefaults(), ...visitorLog });
    form.reset(
      {
        ...visitorLogRawValue,
        id: { value: visitorLogRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VisitorLogFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      visitTimestamp: currentTime,
    };
  }

  private convertVisitorLogRawValueToVisitorLog(
    rawVisitorLog: VisitorLogFormRawValue | NewVisitorLogFormRawValue,
  ): IVisitorLog | NewVisitorLog {
    return {
      ...rawVisitorLog,
      visitTimestamp: dayjs(rawVisitorLog.visitTimestamp, DATE_TIME_FORMAT),
    };
  }

  private convertVisitorLogToVisitorLogRawValue(
    visitorLog: IVisitorLog | (Partial<NewVisitorLog> & VisitorLogFormDefaults),
  ): VisitorLogFormRawValue | PartialWithRequiredKeyOf<NewVisitorLogFormRawValue> {
    return {
      ...visitorLog,
      visitTimestamp: visitorLog.visitTimestamp ? visitorLog.visitTimestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
