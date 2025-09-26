import dayjs from 'dayjs/esm';

import { IContactMessage, NewContactMessage } from './contact-message.model';

export const sampleWithRequiredData: IContactMessage = {
  id: 30205,
  visitorName: 'sentimental tap',
  visitorEmail: 'split whether beside',
  message: '../fake-data/blob/hipster.txt',
  submittedDate: dayjs('2025-08-30T17:46'),
};

export const sampleWithPartialData: IContactMessage = {
  id: 26367,
  visitorName: 'till this',
  visitorEmail: 'without nutty insistent',
  message: '../fake-data/blob/hipster.txt',
  submittedDate: dayjs('2025-08-29T21:51'),
};

export const sampleWithFullData: IContactMessage = {
  id: 7354,
  visitorName: 'kindheartedly striped altruistic',
  visitorEmail: 'elegantly indeed eek',
  message: '../fake-data/blob/hipster.txt',
  submittedDate: dayjs('2025-08-29T21:10'),
};

export const sampleWithNewData: NewContactMessage = {
  visitorName: 'pish generally',
  visitorEmail: 'recuperate visualise next',
  message: '../fake-data/blob/hipster.txt',
  submittedDate: dayjs('2025-08-29T20:22'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
