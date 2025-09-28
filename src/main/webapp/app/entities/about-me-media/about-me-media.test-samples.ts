import { IAboutMeMedia, NewAboutMeMedia } from './about-me-media.model';

export const sampleWithRequiredData: IAboutMeMedia = {
  id: 27663,
  mediaUrl: 'sharply',
  aboutMeMediaType: 'IMAGE',
};

export const sampleWithPartialData: IAboutMeMedia = {
  id: 17205,
  mediaUrl: 'supposing unbearably vicious',
  aboutMeMediaType: 'IMAGE',
  caption: 'accidentally until',
};

export const sampleWithFullData: IAboutMeMedia = {
  id: 12457,
  mediaUrl: 'past till',
  aboutMeMediaType: 'IMAGE',
  caption: 'dwelling gee moonlight',
};

export const sampleWithNewData: NewAboutMeMedia = {
  mediaUrl: 'who ugh',
  aboutMeMediaType: 'IMAGE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
