import { ISkill, NewSkill } from './skill.model';

export const sampleWithRequiredData: ISkill = {
  id: 18455,
  name: 'mmm',
  percentage: 84,
};

export const sampleWithPartialData: ISkill = {
  id: 1169,
  name: 'ew',
  percentage: 9,
};

export const sampleWithFullData: ISkill = {
  id: 24296,
  name: 'as scheme',
  percentage: 5,
};

export const sampleWithNewData: NewSkill = {
  name: 'those throbbing coast',
  percentage: 95,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
