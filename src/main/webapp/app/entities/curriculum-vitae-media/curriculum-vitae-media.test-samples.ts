import { ICurriculumVitaeMedia, NewCurriculumVitaeMedia } from './curriculum-vitae-media.model';

export const sampleWithRequiredData: ICurriculumVitaeMedia = {
  id: 9556,
  mediaUrl: 'charm kiddingly',
  curriculumVitaeMediaType: 'IMAGE',
};

export const sampleWithPartialData: ICurriculumVitaeMedia = {
  id: 10780,
  mediaUrl: 'gleefully amongst',
  curriculumVitaeMediaType: 'VIDEO',
  caption: 'beneath meh forenenst',
};

export const sampleWithFullData: ICurriculumVitaeMedia = {
  id: 4898,
  mediaUrl: 'immediately',
  curriculumVitaeMediaType: 'VIDEO',
  caption: 'past wish',
};

export const sampleWithNewData: NewCurriculumVitaeMedia = {
  mediaUrl: 'bowlful suspicious',
  curriculumVitaeMediaType: 'VIDEO',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
