import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAboutMeMedia, NewAboutMeMedia } from '../about-me-media.model';

export type PartialUpdateAboutMeMedia = Partial<IAboutMeMedia> & Pick<IAboutMeMedia, 'id'>;

export type EntityResponseType = HttpResponse<IAboutMeMedia>;
export type EntityArrayResponseType = HttpResponse<IAboutMeMedia[]>;

@Injectable({ providedIn: 'root' })
export class AboutMeMediaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/about-me-media');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(aboutMeMedia: NewAboutMeMedia): Observable<EntityResponseType> {
    return this.http.post<IAboutMeMedia>(this.resourceUrl, aboutMeMedia, { observe: 'response' });
  }

  update(aboutMeMedia: IAboutMeMedia): Observable<EntityResponseType> {
    return this.http.put<IAboutMeMedia>(`${this.resourceUrl}/${this.getAboutMeMediaIdentifier(aboutMeMedia)}`, aboutMeMedia, {
      observe: 'response',
    });
  }

  partialUpdate(aboutMeMedia: PartialUpdateAboutMeMedia): Observable<EntityResponseType> {
    return this.http.patch<IAboutMeMedia>(`${this.resourceUrl}/${this.getAboutMeMediaIdentifier(aboutMeMedia)}`, aboutMeMedia, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAboutMeMedia>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAboutMeMedia[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAboutMeMediaIdentifier(aboutMeMedia: Pick<IAboutMeMedia, 'id'>): number {
    return aboutMeMedia.id;
  }

  compareAboutMeMedia(o1: Pick<IAboutMeMedia, 'id'> | null, o2: Pick<IAboutMeMedia, 'id'> | null): boolean {
    return o1 && o2 ? this.getAboutMeMediaIdentifier(o1) === this.getAboutMeMediaIdentifier(o2) : o1 === o2;
  }

  addAboutMeMediaToCollectionIfMissing<Type extends Pick<IAboutMeMedia, 'id'>>(
    aboutMeMediaCollection: Type[],
    ...aboutMeMediasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const aboutMeMedias: Type[] = aboutMeMediasToCheck.filter(isPresent);
    if (aboutMeMedias.length > 0) {
      const aboutMeMediaCollectionIdentifiers = aboutMeMediaCollection.map(
        aboutMeMediaItem => this.getAboutMeMediaIdentifier(aboutMeMediaItem)!,
      );
      const aboutMeMediasToAdd = aboutMeMedias.filter(aboutMeMediaItem => {
        const aboutMeMediaIdentifier = this.getAboutMeMediaIdentifier(aboutMeMediaItem);
        if (aboutMeMediaCollectionIdentifiers.includes(aboutMeMediaIdentifier)) {
          return false;
        }
        aboutMeMediaCollectionIdentifiers.push(aboutMeMediaIdentifier);
        return true;
      });
      return [...aboutMeMediasToAdd, ...aboutMeMediaCollection];
    }
    return aboutMeMediaCollection;
  }
}
