import dayjs from 'dayjs/esm';

import { ICurriculumVitae, NewCurriculumVitae } from './curriculum-vitae.model';

export const sampleWithRequiredData: ICurriculumVitae = {
  id: 22000,
  companyName: 'before',
  language: 'SR',
  jobDescriptionHTML: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: ICurriculumVitae = {
  id: 3942,
  companyName: 'vice',
  language: 'SR',
  jobDescriptionHTML: '../fake-data/blob/hipster.txt',
  startDate: dayjs('2024-08-29'),
  status: 'STILLACTIVE',
  category: 'blissfully',
};

export const sampleWithFullData: ICurriculumVitae = {
  id: 12680,
  companyName: 'beyond',
  language: 'EN',
  jobDescriptionHTML: '../fake-data/blob/hipster.txt',
  startDate: dayjs('2024-08-30'),
  endDate: dayjs('2024-08-29'),
  status: 'FINISHED',
  type: 'FREELANCING',
  category: 'margarine',
};

export const sampleWithNewData: NewCurriculumVitae = {
  companyName: 'whoever trip frizzy',
  language: 'EN',
  jobDescriptionHTML: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
