import { IAboutMeMedia, NewAboutMeMedia } from './about-me-media.model';

export const sampleWithRequiredData: IAboutMeMedia = {
  id: 6164,
  mediaUrl: 'energetically until aw',
  aboutMeMediaType: 'VIDEO',
};

export const sampleWithPartialData: IAboutMeMedia = {
  id: 10702,
  mediaUrl: 'ew yahoo',
  aboutMeMediaType: 'IMAGE',
  caption: 'distant',
};

export const sampleWithFullData: IAboutMeMedia = {
  id: 3822,
  mediaUrl: 'underneath',
  aboutMeMediaType: 'VIDEO',
  caption: 'whose',
};

export const sampleWithNewData: NewAboutMeMedia = {
  mediaUrl: 'quarrelsome infuriate apropos',
  aboutMeMediaType: 'VIDEO',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
