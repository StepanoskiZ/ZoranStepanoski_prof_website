import { IBusinessService } from 'app/entities/business-service/business-service.model';
import { UnifiedMediaType } from 'app/entities/enumerations/unified-media-type.model';

export interface IBusinessServiceMedia {
  id: number;
  mediaUrl?: string | null;
  businessServiceMediaType?: keyof typeof UnifiedMediaType | null;
  caption?: string | null;
  businessService?: Pick<IBusinessService, 'id'> | null;
}

export type NewBusinessServiceMedia = Omit<IBusinessServiceMedia, 'id'> & { id: null };
