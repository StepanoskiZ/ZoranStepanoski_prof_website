import { IBusinessServiceMedia, NewBusinessServiceMedia } from './business-service-media.model';

export const sampleWithRequiredData: IBusinessServiceMedia = {
  id: 10816,
  mediaUrl: 'similarity kindheartedly',
  businessServiceMediaType: 'VIDEO',
};

export const sampleWithPartialData: IBusinessServiceMedia = {
  id: 1455,
  mediaUrl: 'excluding until apropos',
  businessServiceMediaType: 'IMAGE',
  caption: 'bitter inasmuch',
};

export const sampleWithFullData: IBusinessServiceMedia = {
  id: 14490,
  mediaUrl: 'sadly',
  businessServiceMediaType: 'IMAGE',
  caption: 'poor',
};

export const sampleWithNewData: NewBusinessServiceMedia = {
  mediaUrl: 'ah safely',
  businessServiceMediaType: 'VIDEO',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
