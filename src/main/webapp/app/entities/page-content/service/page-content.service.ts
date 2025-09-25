import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPageContent, NewPageContent } from '../page-content.model';

export type PartialUpdatePageContent = Partial<IPageContent> & Pick<IPageContent, 'id'>;

export type EntityResponseType = HttpResponse<IPageContent>;
export type EntityArrayResponseType = HttpResponse<IPageContent[]>;

@Injectable({ providedIn: 'root' })
export class PageContentService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/page-contents');

  create(pageContent: NewPageContent): Observable<EntityResponseType> {
    return this.http.post<IPageContent>(this.resourceUrl, pageContent, { observe: 'response' });
  }

  update(pageContent: IPageContent): Observable<EntityResponseType> {
    return this.http.put<IPageContent>(`${this.resourceUrl}/${this.getPageContentIdentifier(pageContent)}`, pageContent, {
      observe: 'response',
    });
  }

  partialUpdate(pageContent: PartialUpdatePageContent): Observable<EntityResponseType> {
    return this.http.patch<IPageContent>(`${this.resourceUrl}/${this.getPageContentIdentifier(pageContent)}`, pageContent, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPageContent>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPageContent[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPageContentIdentifier(pageContent: Pick<IPageContent, 'id'>): number {
    return pageContent.id;
  }

  comparePageContent(o1: Pick<IPageContent, 'id'> | null, o2: Pick<IPageContent, 'id'> | null): boolean {
    return o1 && o2 ? this.getPageContentIdentifier(o1) === this.getPageContentIdentifier(o2) : o1 === o2;
  }

  addPageContentToCollectionIfMissing<Type extends Pick<IPageContent, 'id'>>(
    pageContentCollection: Type[],
    ...pageContentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pageContents: Type[] = pageContentsToCheck.filter(isPresent);
    if (pageContents.length > 0) {
      const pageContentCollectionIdentifiers = pageContentCollection.map(pageContentItem => this.getPageContentIdentifier(pageContentItem));
      const pageContentsToAdd = pageContents.filter(pageContentItem => {
        const pageContentIdentifier = this.getPageContentIdentifier(pageContentItem);
        if (pageContentCollectionIdentifiers.includes(pageContentIdentifier)) {
          return false;
        }
        pageContentCollectionIdentifiers.push(pageContentIdentifier);
        return true;
      });
      return [...pageContentsToAdd, ...pageContentCollection];
    }
    return pageContentCollection;
  }
}
