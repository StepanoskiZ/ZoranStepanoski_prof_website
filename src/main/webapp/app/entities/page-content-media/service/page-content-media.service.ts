import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPageContentMedia, NewPageContentMedia } from '../page-content-media.model';

export type PartialUpdatePageContentMedia = Partial<IPageContentMedia> & Pick<IPageContentMedia, 'id'>;

export type EntityResponseType = HttpResponse<IPageContentMedia>;
export type EntityArrayResponseType = HttpResponse<IPageContentMedia[]>;

@Injectable({ providedIn: 'root' })
export class PageContentMediaService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/page-content-medias');

  create(pageContentMedia: NewPageContentMedia): Observable<EntityResponseType> {
    return this.http.post<IPageContentMedia>(this.resourceUrl, pageContentMedia, { observe: 'response' });
  }

  update(pageContentMedia: IPageContentMedia): Observable<EntityResponseType> {
    return this.http.put<IPageContentMedia>(
      `${this.resourceUrl}/${this.getPageContentMediaIdentifier(pageContentMedia)}`,
      pageContentMedia,
      { observe: 'response' },
    );
  }

  partialUpdate(pageContentMedia: PartialUpdatePageContentMedia): Observable<EntityResponseType> {
    return this.http.patch<IPageContentMedia>(
      `${this.resourceUrl}/${this.getPageContentMediaIdentifier(pageContentMedia)}`,
      pageContentMedia,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPageContentMedia>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPageContentMedia[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPageContentMediaIdentifier(pageContentMedia: Pick<IPageContentMedia, 'id'>): number {
    return pageContentMedia.id;
  }

  comparePageContentMedia(o1: Pick<IPageContentMedia, 'id'> | null, o2: Pick<IPageContentMedia, 'id'> | null): boolean {
    return o1 && o2 ? this.getPageContentMediaIdentifier(o1) === this.getPageContentMediaIdentifier(o2) : o1 === o2;
  }

  addPageContentMediaToCollectionIfMissing<Type extends Pick<IPageContentMedia, 'id'>>(
    pageContentMediaCollection: Type[],
    ...pageContentMediasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pageContentMedias: Type[] = pageContentMediasToCheck.filter(isPresent);
    if (pageContentMedias.length > 0) {
      const pageContentMediaCollectionIdentifiers = pageContentMediaCollection.map(pageContentMediaItem =>
        this.getPageContentMediaIdentifier(pageContentMediaItem),
      );
      const pageContentMediasToAdd = pageContentMedias.filter(pageContentMediaItem => {
        const pageContentMediaIdentifier = this.getPageContentMediaIdentifier(pageContentMediaItem);
        if (pageContentMediaCollectionIdentifiers.includes(pageContentMediaIdentifier)) {
          return false;
        }
        pageContentMediaCollectionIdentifiers.push(pageContentMediaIdentifier);
        return true;
      });
      return [...pageContentMediasToAdd, ...pageContentMediaCollection];
    }
    return pageContentMediaCollection;
  }
}
