import dayjs from 'dayjs/esm';

import { ICurriculumVitae, NewCurriculumVitae } from './curriculum-vitae.model';

export const sampleWithRequiredData: ICurriculumVitae = {
  id: 773,
  companyName: 'suddenly',
  language: 'EN',
  jobDescriptionHTML: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: ICurriculumVitae = {
  id: 4128,
  companyName: 'astronomy',
  language: 'SR',
  jobDescriptionHTML: '../fake-data/blob/hipster.txt',
  startDate: dayjs('2024-08-29'),
  endDate: dayjs('2024-08-29'),
  category: 'accentuate eek',
};

export const sampleWithFullData: ICurriculumVitae = {
  id: 13902,
  companyName: 'separately fooey',
  language: 'EN',
  jobDescriptionHTML: '../fake-data/blob/hipster.txt',
  startDate: dayjs('2024-08-30'),
  endDate: dayjs('2024-08-29'),
  status: 'STILLACTIVE',
  type: 'CONTRACT',
  category: 'pall',
};

export const sampleWithNewData: NewCurriculumVitae = {
  companyName: 'wrongly wig',
  language: 'EN',
  jobDescriptionHTML: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
