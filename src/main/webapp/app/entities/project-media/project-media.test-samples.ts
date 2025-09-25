import { IProjectMedia, NewProjectMedia } from './project-media.model';

export const sampleWithRequiredData: IProjectMedia = {
  id: 3645,
  mediaUrl: 'pomelo option safeguard',
  projectMediaType: 'VIDEO',
};

export const sampleWithPartialData: IProjectMedia = {
  id: 28754,
  mediaUrl: 'huzzah',
  projectMediaType: 'VIDEO',
};

export const sampleWithFullData: IProjectMedia = {
  id: 16800,
  mediaUrl: 'haversack frizz gosh',
  projectMediaType: 'VIDEO',
  caption: 'square',
};

export const sampleWithNewData: NewProjectMedia = {
  mediaUrl: 'source which coarse',
  projectMediaType: 'VIDEO',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
