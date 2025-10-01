import dayjs from 'dayjs/esm';

import { IProject, NewProject } from './project.model';

export const sampleWithRequiredData: IProject = {
  id: 9505,
  title: 'because justice',
  descriptionHTML: '../fake-data/blob/hipster.txt',
  language: 'EN',
};

export const sampleWithPartialData: IProject = {
  id: 21392,
  title: 'promptly',
  descriptionHTML: '../fake-data/blob/hipster.txt',
  startDate: dayjs('2025-08-30'),
  endDate: dayjs('2025-08-30'),
  projectUrl: 'um blissfully which',
  language: 'SR',
};

export const sampleWithFullData: IProject = {
  id: 30742,
  title: 'gently reskill',
  descriptionHTML: '../fake-data/blob/hipster.txt',
  startDate: dayjs('2025-08-30'),
  endDate: dayjs('2025-08-29'),
  status: 'COMPLETED',
  projectUrl: 'pleasing online',
  category: 'handicap brr judgementally',
  language: 'SR',
};

export const sampleWithNewData: NewProject = {
  title: 'er caring',
  descriptionHTML: '../fake-data/blob/hipster.txt',
  language: 'SR',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
