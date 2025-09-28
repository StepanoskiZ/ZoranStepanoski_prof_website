import dayjs from 'dayjs/esm';

import { IProject, NewProject } from './project.model';

export const sampleWithRequiredData: IProject = {
  id: 11838,
  title: 'sealift',
  descriptionHTML: '../fake-data/blob/hipster.txt',
  language: 'EN',
};

export const sampleWithPartialData: IProject = {
  id: 7739,
  title: 'instantly pace apropos',
  descriptionHTML: '../fake-data/blob/hipster.txt',
  endDate: dayjs('2025-08-30'),
  projectUrl: 'meh ouch',
  category: 'mollycoddle',
  language: 'SR',
};

export const sampleWithFullData: IProject = {
  id: 5372,
  title: 'about quirkily questioningly',
  descriptionHTML: '../fake-data/blob/hipster.txt',
  startDate: dayjs('2025-08-30'),
  endDate: dayjs('2025-08-30'),
  status: 'COMPLETED',
  projectUrl: 'acquaint an',
  category: 'left if briefly',
  language: 'SR',
};

export const sampleWithNewData: NewProject = {
  title: 'aha if',
  descriptionHTML: '../fake-data/blob/hipster.txt',
  language: 'SR',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
