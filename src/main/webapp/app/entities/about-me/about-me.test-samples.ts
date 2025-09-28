import { IAboutMe, NewAboutMe } from './about-me.model';

export const sampleWithRequiredData: IAboutMe = {
  id: 29293,
  contentHtml: '../fake-data/blob/hipster.txt',
  language: 'SR',
};

export const sampleWithPartialData: IAboutMe = {
  id: 2561,
  contentHtml: '../fake-data/blob/hipster.txt',
  language: 'EN',
};

export const sampleWithFullData: IAboutMe = {
  id: 5023,
  contentHtml: '../fake-data/blob/hipster.txt',
  language: 'SR',
};

export const sampleWithNewData: NewAboutMe = {
  contentHtml: '../fake-data/blob/hipster.txt',
  language: 'SR',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
