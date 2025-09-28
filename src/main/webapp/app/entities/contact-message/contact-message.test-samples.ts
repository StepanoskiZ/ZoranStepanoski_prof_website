import dayjs from 'dayjs/esm';

import { IContactMessage, NewContactMessage } from './contact-message.model';

export const sampleWithRequiredData: IContactMessage = {
  id: 31379,
  visitorName: 'likewise similar cool',
  visitorEmail: 'huzzah fan',
  message: '../fake-data/blob/hipster.txt',
  submittedDate: dayjs('2025-08-29T19:32'),
};

export const sampleWithPartialData: IContactMessage = {
  id: 19251,
  visitorName: 'scarcely',
  visitorEmail: 'toward yuck',
  message: '../fake-data/blob/hipster.txt',
  submittedDate: dayjs('2025-08-29T22:07'),
};

export const sampleWithFullData: IContactMessage = {
  id: 2987,
  visitorName: 'well beside',
  visitorEmail: 'plateau brr brr',
  message: '../fake-data/blob/hipster.txt',
  submittedDate: dayjs('2025-08-30T14:28'),
};

export const sampleWithNewData: NewContactMessage = {
  visitorName: 'compass till once',
  visitorEmail: 'blah',
  message: '../fake-data/blob/hipster.txt',
  submittedDate: dayjs('2025-08-30T17:31'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
