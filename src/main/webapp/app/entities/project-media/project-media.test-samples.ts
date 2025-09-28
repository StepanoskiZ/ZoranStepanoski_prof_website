import { IProjectMedia, NewProjectMedia } from './project-media.model';

export const sampleWithRequiredData: IProjectMedia = {
  id: 7079,
  mediaUrl: 'veal',
  projectMediaType: 'IMAGE',
};

export const sampleWithPartialData: IProjectMedia = {
  id: 19380,
  mediaUrl: 'message',
  projectMediaType: 'VIDEO',
};

export const sampleWithFullData: IProjectMedia = {
  id: 20419,
  mediaUrl: 'lysine along clean',
  projectMediaType: 'VIDEO',
  caption: 'selling',
};

export const sampleWithNewData: NewProjectMedia = {
  mediaUrl: 'furthermore afterwards',
  projectMediaType: 'IMAGE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
