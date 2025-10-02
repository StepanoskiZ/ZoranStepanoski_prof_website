import { ISkill, NewSkill } from './skill.model';

export const sampleWithRequiredData: ISkill = {
  id: 27843,
  name: 'the apud',
  yearsOfExperience: 21830,
};

export const sampleWithPartialData: ISkill = {
  id: 21242,
  name: 'direct what per',
  yearsOfExperience: 8555,
};

export const sampleWithFullData: ISkill = {
  id: 12676,
  name: 'issue boo officially',
  yearsOfExperience: 12179,
};

export const sampleWithNewData: NewSkill = {
  name: 'inwardly whoever grant',
  yearsOfExperience: 2934,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
