import { ISkill, NewSkill } from './skill.model';

export const sampleWithRequiredData: ISkill = {
  id: 175,
  name: 'providence',
  percentage: 97,
};

export const sampleWithPartialData: ISkill = {
  id: 5786,
  name: 'after evenly apropos',
  percentage: 47,
};

export const sampleWithFullData: ISkill = {
  id: 13186,
  name: 'bow for',
  percentage: 15,
};

export const sampleWithNewData: NewSkill = {
  name: 'since why watch',
  percentage: 93,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
