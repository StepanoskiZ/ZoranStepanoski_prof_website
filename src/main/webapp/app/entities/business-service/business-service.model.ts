export interface IBusinessService {
  id: number;
  title?: string | null;
  descriptionHTML?: string | null;
  icon?: string | null;
}

export type NewBusinessService = Omit<IBusinessService, 'id'> & { id: null };
