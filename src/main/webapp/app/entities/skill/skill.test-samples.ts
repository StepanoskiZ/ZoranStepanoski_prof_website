import { ISkill, NewSkill } from './skill.model';

export const sampleWithRequiredData: ISkill = {
  id: 27843,
  name: 'the apud',
  percentage: 67,
};

export const sampleWithPartialData: ISkill = {
  id: 21242,
  name: 'direct what per',
  percentage: 26,
};

export const sampleWithFullData: ISkill = {
  id: 12676,
  name: 'issue boo officially',
  percentage: 37,
};

export const sampleWithNewData: NewSkill = {
  name: 'inwardly whoever grant',
  percentage: 9,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
