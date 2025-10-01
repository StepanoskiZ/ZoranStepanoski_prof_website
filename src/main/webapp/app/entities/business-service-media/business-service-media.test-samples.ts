import { IBusinessServiceMedia, NewBusinessServiceMedia } from './business-service-media.model';

export const sampleWithRequiredData: IBusinessServiceMedia = {
  id: 17287,
  mediaUrl: 'jovially finally after',
  businessServiceMediaType: 'VIDEO',
};

export const sampleWithPartialData: IBusinessServiceMedia = {
  id: 13703,
  mediaUrl: 'slum tote',
  businessServiceMediaType: 'VIDEO',
  caption: 'mmm instead',
};

export const sampleWithFullData: IBusinessServiceMedia = {
  id: 14610,
  mediaUrl: 'arctic woodshed readily',
  businessServiceMediaType: 'IMAGE',
  caption: 'wallet ouch whoever',
};

export const sampleWithNewData: NewBusinessServiceMedia = {
  mediaUrl: 'elderly kind apud',
  businessServiceMediaType: 'IMAGE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
