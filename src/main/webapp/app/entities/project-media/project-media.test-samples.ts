import { IProjectMedia, NewProjectMedia } from './project-media.model';

export const sampleWithRequiredData: IProjectMedia = {
  id: 2021,
  mediaUrl: 'rubbery',
  projectMediaType: 'VIDEO',
};

export const sampleWithPartialData: IProjectMedia = {
  id: 31765,
  mediaUrl: 'what',
  projectMediaType: 'IMAGE',
};

export const sampleWithFullData: IProjectMedia = {
  id: 24299,
  mediaUrl: 'sparkling denominator criminal',
  projectMediaType: 'VIDEO',
  caption: 'feline whenever eek',
};

export const sampleWithNewData: NewProjectMedia = {
  mediaUrl: 'to likewise yowza',
  projectMediaType: 'VIDEO',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
