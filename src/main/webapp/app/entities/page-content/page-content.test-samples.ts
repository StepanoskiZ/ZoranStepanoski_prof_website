import { IPageContent, NewPageContent } from './page-content.model';

export const sampleWithRequiredData: IPageContent = {
  id: 32411,
  sectionKey: 'instead',
  contentHtml: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IPageContent = {
  id: 5986,
  sectionKey: 'rise palatable',
  contentHtml: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IPageContent = {
  id: 29492,
  sectionKey: 'knowledgeably',
  contentHtml: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewPageContent = {
  sectionKey: 'yahoo stealthily hypothesise',
  contentHtml: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
