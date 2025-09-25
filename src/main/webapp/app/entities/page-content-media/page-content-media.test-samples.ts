import { IPageContentMedia, NewPageContentMedia } from './page-content-media.model';

export const sampleWithRequiredData: IPageContentMedia = {
  id: 27803,
  mediaUrl: 'mid thorny accept',
  pageContentMediaType: 'IMAGE',
};

export const sampleWithPartialData: IPageContentMedia = {
  id: 30880,
  mediaUrl: 'whoever',
  pageContentMediaType: 'IMAGE',
};

export const sampleWithFullData: IPageContentMedia = {
  id: 20484,
  mediaUrl: 'maroon dark',
  pageContentMediaType: 'VIDEO',
  caption: 'woot',
};

export const sampleWithNewData: NewPageContentMedia = {
  mediaUrl: 'gadzooks cheetah',
  pageContentMediaType: 'VIDEO',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
