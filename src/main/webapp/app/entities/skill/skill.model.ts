export interface ISkill {
  id: number;
  name?: string | null;
  percentage?: number | null;
}

export type NewSkill = Omit<ISkill, 'id'> & { id: null };
