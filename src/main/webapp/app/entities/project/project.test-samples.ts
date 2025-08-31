import dayjs from 'dayjs/esm';

import { IProject, NewProject } from './project.model';

export const sampleWithRequiredData: IProject = {
  id: 22823,
  title: 'anxiously',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IProject = {
  id: 12852,
  title: 'likewise',
  description: '../fake-data/blob/hipster.txt',
  endDate: dayjs('2025-08-29'),
};

export const sampleWithFullData: IProject = {
  id: 1375,
  title: 'tray pack hydrolyze',
  description: '../fake-data/blob/hipster.txt',
  startDate: dayjs('2025-08-30'),
  endDate: dayjs('2025-08-30'),
  status: 'COMPLETED',
  projectUrl: 'emerge whether across',
  category: 'robust silver',
};

export const sampleWithNewData: NewProject = {
  title: 'zowie vice',
  description: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
