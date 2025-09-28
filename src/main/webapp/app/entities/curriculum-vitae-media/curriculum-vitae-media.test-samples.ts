import { ICurriculumVitaeMedia, NewCurriculumVitaeMedia } from './curriculum-vitae-media.model';

export const sampleWithRequiredData: ICurriculumVitaeMedia = {
  id: 20683,
  mediaUrl: 'cashier',
  curriculumVitaeMediaType: 'IMAGE',
};

export const sampleWithPartialData: ICurriculumVitaeMedia = {
  id: 17072,
  mediaUrl: 'lest',
  curriculumVitaeMediaType: 'VIDEO',
  caption: 'improbable',
};

export const sampleWithFullData: ICurriculumVitaeMedia = {
  id: 4616,
  mediaUrl: 'primary revolution towards',
  curriculumVitaeMediaType: 'IMAGE',
  caption: 'unlike corridor tram',
};

export const sampleWithNewData: NewCurriculumVitaeMedia = {
  mediaUrl: 'concuss impartial',
  curriculumVitaeMediaType: 'IMAGE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
