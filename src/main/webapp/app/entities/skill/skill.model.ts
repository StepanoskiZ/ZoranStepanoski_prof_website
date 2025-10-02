export interface ISkill {
  id: number;
  name?: string | null;
  yearsOfExperience?: number | null;
}

export type NewSkill = Omit<ISkill, 'id'> & { id: null };
