import { ISkill, NewSkill } from './skill.model';

export const sampleWithRequiredData: ISkill = {
  id: 16979,
  name: 'if',
  percentage: 22,
};

export const sampleWithPartialData: ISkill = {
  id: 2828,
  name: 'wisdom really coaxingly',
  percentage: 44,
};

export const sampleWithFullData: ISkill = {
  id: 27953,
  name: 'and fooey hourly',
  percentage: 81,
};

export const sampleWithNewData: NewSkill = {
  name: 'closely application case',
  percentage: 47,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
