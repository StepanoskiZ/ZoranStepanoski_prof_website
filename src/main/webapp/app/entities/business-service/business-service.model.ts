import { IBusinessServiceMedia } from 'app/entities/business-service-media/business-service-media.model';

export interface IBusinessService {
  id: number;
  title?: string | null;
  descriptionHTML?: string | null;
  icon?: string | null;
  media?: Pick<IBusinessServiceMedia, 'id' | 'mediaUrl'>[] | null;
}

export type NewBusinessService = Omit<IBusinessService, 'id'> & { id: null };
