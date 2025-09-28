export interface IAuthority {
  id: number;
}

export type NewAuthority = Omit<IAuthority, 'id'> & { id: null };
