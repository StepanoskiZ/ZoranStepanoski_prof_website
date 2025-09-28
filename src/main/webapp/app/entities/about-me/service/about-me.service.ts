import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAboutMe, NewAboutMe } from '../about-me.model';

export type PartialUpdateAboutMe = Partial<IAboutMe> & Pick<IAboutMe, 'id'>;

export type EntityResponseType = HttpResponse<IAboutMe>;
export type EntityArrayResponseType = HttpResponse<IAboutMe[]>;

@Injectable({ providedIn: 'root' })
export class AboutMeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/about-mes');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(aboutMe: NewAboutMe): Observable<EntityResponseType> {
    return this.http.post<IAboutMe>(this.resourceUrl, aboutMe, { observe: 'response' });
  }

  update(aboutMe: IAboutMe): Observable<EntityResponseType> {
    return this.http.put<IAboutMe>(`${this.resourceUrl}/${this.getAboutMeIdentifier(aboutMe)}`, aboutMe, { observe: 'response' });
  }

  partialUpdate(aboutMe: PartialUpdateAboutMe): Observable<EntityResponseType> {
    return this.http.patch<IAboutMe>(`${this.resourceUrl}/${this.getAboutMeIdentifier(aboutMe)}`, aboutMe, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAboutMe>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAboutMe[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAboutMeIdentifier(aboutMe: Pick<IAboutMe, 'id'>): number {
    return aboutMe.id;
  }

  compareAboutMe(o1: Pick<IAboutMe, 'id'> | null, o2: Pick<IAboutMe, 'id'> | null): boolean {
    return o1 && o2 ? this.getAboutMeIdentifier(o1) === this.getAboutMeIdentifier(o2) : o1 === o2;
  }

  addAboutMeToCollectionIfMissing<Type extends Pick<IAboutMe, 'id'>>(
    aboutMeCollection: Type[],
    ...aboutMesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const aboutMes: Type[] = aboutMesToCheck.filter(isPresent);
    if (aboutMes.length > 0) {
      const aboutMeCollectionIdentifiers = aboutMeCollection.map(aboutMeItem => this.getAboutMeIdentifier(aboutMeItem)!);
      const aboutMesToAdd = aboutMes.filter(aboutMeItem => {
        const aboutMeIdentifier = this.getAboutMeIdentifier(aboutMeItem);
        if (aboutMeCollectionIdentifiers.includes(aboutMeIdentifier)) {
          return false;
        }
        aboutMeCollectionIdentifiers.push(aboutMeIdentifier);
        return true;
      });
      return [...aboutMesToAdd, ...aboutMeCollection];
    }
    return aboutMeCollection;
  }
}
