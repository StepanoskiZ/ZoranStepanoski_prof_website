import { IPageContentMedia, NewPageContentMedia } from './page-content-media.model';

export const sampleWithRequiredData: IPageContentMedia = {
  id: 27190,
  mediaUrl: 'aha whenever hence',
  pageContentMediaType: 'VIDEO',
};

export const sampleWithPartialData: IPageContentMedia = {
  id: 17433,
  mediaUrl: 'next yippee yearly',
  pageContentMediaType: 'VIDEO',
  caption: 'ha though coordination',
};

export const sampleWithFullData: IPageContentMedia = {
  id: 26235,
  mediaUrl: 'briefly ocean result',
  pageContentMediaType: 'IMAGE',
  caption: 'verbally occasion elderly',
};

export const sampleWithNewData: NewPageContentMedia = {
  mediaUrl: 'bah might cry',
  pageContentMediaType: 'VIDEO',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
