import { IAboutMe, NewAboutMe } from './about-me.model';

export const sampleWithRequiredData: IAboutMe = {
  id: 1863,
  contentHtml: '../fake-data/blob/hipster.txt',
  language: 'SR',
};

export const sampleWithPartialData: IAboutMe = {
  id: 23466,
  contentHtml: '../fake-data/blob/hipster.txt',
  language: 'EN',
};

export const sampleWithFullData: IAboutMe = {
  id: 18080,
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
