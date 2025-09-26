import dayjs from 'dayjs/esm';

import { IProject, NewProject } from './project.model';

export const sampleWithRequiredData: IProject = {
  id: 16800,
  title: 'dimly',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IProject = {
  id: 2347,
  title: 'though',
  description: '../fake-data/blob/hipster.txt',
  status: 'COMPLETED',
  projectUrl: 'against duh',
};

export const sampleWithFullData: IProject = {
  id: 30859,
  title: 'yowza gloom swimming',
  description: '../fake-data/blob/hipster.txt',
  startDate: dayjs('2025-08-30'),
  endDate: dayjs('2025-08-30'),
  status: 'COMPLETED',
  projectUrl: 'whose officially',
  category: 'far amongst',
};

export const sampleWithNewData: NewProject = {
  title: 'aged',
  description: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
