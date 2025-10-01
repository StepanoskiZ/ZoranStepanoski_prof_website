import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBusinessServiceMedia, NewBusinessServiceMedia } from '../business-service-media.model';

export type PartialUpdateBusinessServiceMedia = Partial<IBusinessServiceMedia> & Pick<IBusinessServiceMedia, 'id'>;

export type EntityResponseType = HttpResponse<IBusinessServiceMedia>;
export type EntityArrayResponseType = HttpResponse<IBusinessServiceMedia[]>;

@Injectable({ providedIn: 'root' })
export class BusinessServiceMediaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/business-service-medias');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(businessServiceMedia: NewBusinessServiceMedia): Observable<EntityResponseType> {
    return this.http.post<IBusinessServiceMedia>(this.resourceUrl, businessServiceMedia, { observe: 'response' });
  }

  update(businessServiceMedia: IBusinessServiceMedia): Observable<EntityResponseType> {
    return this.http.put<IBusinessServiceMedia>(
      `${this.resourceUrl}/${this.getBusinessServiceMediaIdentifier(businessServiceMedia)}`,
      businessServiceMedia,
      { observe: 'response' },
    );
  }

  partialUpdate(businessServiceMedia: PartialUpdateBusinessServiceMedia): Observable<EntityResponseType> {
    return this.http.patch<IBusinessServiceMedia>(
      `${this.resourceUrl}/${this.getBusinessServiceMediaIdentifier(businessServiceMedia)}`,
      businessServiceMedia,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBusinessServiceMedia>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBusinessServiceMedia[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBusinessServiceMediaIdentifier(businessServiceMedia: Pick<IBusinessServiceMedia, 'id'>): number {
    return businessServiceMedia.id;
  }

  compareBusinessServiceMedia(o1: Pick<IBusinessServiceMedia, 'id'> | null, o2: Pick<IBusinessServiceMedia, 'id'> | null): boolean {
    return o1 && o2 ? this.getBusinessServiceMediaIdentifier(o1) === this.getBusinessServiceMediaIdentifier(o2) : o1 === o2;
  }

  addBusinessServiceMediaToCollectionIfMissing<Type extends Pick<IBusinessServiceMedia, 'id'>>(
    businessServiceMediaCollection: Type[],
    ...businessServiceMediasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const businessServiceMedias: Type[] = businessServiceMediasToCheck.filter(isPresent);
    if (businessServiceMedias.length > 0) {
      const businessServiceMediaCollectionIdentifiers = businessServiceMediaCollection.map(
        businessServiceMediaItem => this.getBusinessServiceMediaIdentifier(businessServiceMediaItem)!,
      );
      const businessServiceMediasToAdd = businessServiceMedias.filter(businessServiceMediaItem => {
        const businessServiceMediaIdentifier = this.getBusinessServiceMediaIdentifier(businessServiceMediaItem);
        if (businessServiceMediaCollectionIdentifiers.includes(businessServiceMediaIdentifier)) {
          return false;
        }
        businessServiceMediaCollectionIdentifiers.push(businessServiceMediaIdentifier);
        return true;
      });
      return [...businessServiceMediasToAdd, ...businessServiceMediaCollection];
    }
    return businessServiceMediaCollection;
  }
}
