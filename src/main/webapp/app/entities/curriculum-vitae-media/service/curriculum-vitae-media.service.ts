import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICurriculumVitaeMedia, NewCurriculumVitaeMedia } from '../curriculum-vitae-media.model';

export type PartialUpdateCurriculumVitaeMedia = Partial<ICurriculumVitaeMedia> & Pick<ICurriculumVitaeMedia, 'id'>;

export type EntityResponseType = HttpResponse<ICurriculumVitaeMedia>;
export type EntityArrayResponseType = HttpResponse<ICurriculumVitaeMedia[]>;

@Injectable({ providedIn: 'root' })
export class CurriculumVitaeMediaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/curriculum-vitae-medias');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(curriculumVitaeMedia: NewCurriculumVitaeMedia): Observable<EntityResponseType> {
    return this.http.post<ICurriculumVitaeMedia>(this.resourceUrl, curriculumVitaeMedia, { observe: 'response' });
  }

  update(curriculumVitaeMedia: ICurriculumVitaeMedia): Observable<EntityResponseType> {
    return this.http.put<ICurriculumVitaeMedia>(
      `${this.resourceUrl}/${this.getCurriculumVitaeMediaIdentifier(curriculumVitaeMedia)}`,
      curriculumVitaeMedia,
      { observe: 'response' },
    );
  }

  partialUpdate(curriculumVitaeMedia: PartialUpdateCurriculumVitaeMedia): Observable<EntityResponseType> {
    return this.http.patch<ICurriculumVitaeMedia>(
      `${this.resourceUrl}/${this.getCurriculumVitaeMediaIdentifier(curriculumVitaeMedia)}`,
      curriculumVitaeMedia,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICurriculumVitaeMedia>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICurriculumVitaeMedia[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCurriculumVitaeMediaIdentifier(curriculumVitaeMedia: Pick<ICurriculumVitaeMedia, 'id'>): number {
    return curriculumVitaeMedia.id;
  }

  compareCurriculumVitaeMedia(o1: Pick<ICurriculumVitaeMedia, 'id'> | null, o2: Pick<ICurriculumVitaeMedia, 'id'> | null): boolean {
    return o1 && o2 ? this.getCurriculumVitaeMediaIdentifier(o1) === this.getCurriculumVitaeMediaIdentifier(o2) : o1 === o2;
  }

  addCurriculumVitaeMediaToCollectionIfMissing<Type extends Pick<ICurriculumVitaeMedia, 'id'>>(
    curriculumVitaeMediaCollection: Type[],
    ...curriculumVitaeMediasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const curriculumVitaeMedias: Type[] = curriculumVitaeMediasToCheck.filter(isPresent);
    if (curriculumVitaeMedias.length > 0) {
      const curriculumVitaeMediaCollectionIdentifiers = curriculumVitaeMediaCollection.map(
        curriculumVitaeMediaItem => this.getCurriculumVitaeMediaIdentifier(curriculumVitaeMediaItem)!,
      );
      const curriculumVitaeMediasToAdd = curriculumVitaeMedias.filter(curriculumVitaeMediaItem => {
        const curriculumVitaeMediaIdentifier = this.getCurriculumVitaeMediaIdentifier(curriculumVitaeMediaItem);
        if (curriculumVitaeMediaCollectionIdentifiers.includes(curriculumVitaeMediaIdentifier)) {
          return false;
        }
        curriculumVitaeMediaCollectionIdentifiers.push(curriculumVitaeMediaIdentifier);
        return true;
      });
      return [...curriculumVitaeMediasToAdd, ...curriculumVitaeMediaCollection];
    }
    return curriculumVitaeMediaCollection;
  }
}
