import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICurriculumVitae, NewCurriculumVitae } from '../curriculum-vitae.model';

export type PartialUpdateCurriculumVitae = Partial<ICurriculumVitae> & Pick<ICurriculumVitae, 'id'>;

type RestOf<T extends ICurriculumVitae | NewCurriculumVitae> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestCurriculumVitae = RestOf<ICurriculumVitae>;

export type NewRestCurriculumVitae = RestOf<NewCurriculumVitae>;

export type PartialUpdateRestCurriculumVitae = RestOf<PartialUpdateCurriculumVitae>;

export type EntityResponseType = HttpResponse<ICurriculumVitae>;
export type EntityArrayResponseType = HttpResponse<ICurriculumVitae[]>;

@Injectable({ providedIn: 'root' })
export class CurriculumVitaeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/curriculum-vitae');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(curriculumVitae: NewCurriculumVitae): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(curriculumVitae);
    return this.http
      .post<RestCurriculumVitae>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(curriculumVitae: ICurriculumVitae): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(curriculumVitae);
    return this.http
      .put<RestCurriculumVitae>(`${this.resourceUrl}/${this.getCurriculumVitaeIdentifier(curriculumVitae)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(curriculumVitae: PartialUpdateCurriculumVitae): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(curriculumVitae);
    return this.http
      .patch<RestCurriculumVitae>(`${this.resourceUrl}/${this.getCurriculumVitaeIdentifier(curriculumVitae)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCurriculumVitae>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCurriculumVitae[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCurriculumVitaeIdentifier(curriculumVitae: Pick<ICurriculumVitae, 'id'>): number {
    return curriculumVitae.id;
  }

  compareCurriculumVitae(o1: Pick<ICurriculumVitae, 'id'> | null, o2: Pick<ICurriculumVitae, 'id'> | null): boolean {
    return o1 && o2 ? this.getCurriculumVitaeIdentifier(o1) === this.getCurriculumVitaeIdentifier(o2) : o1 === o2;
  }

  addCurriculumVitaeToCollectionIfMissing<Type extends Pick<ICurriculumVitae, 'id'>>(
    curriculumVitaeCollection: Type[],
    ...curriculumVitaesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const curriculumVitaes: Type[] = curriculumVitaesToCheck.filter(isPresent);
    if (curriculumVitaes.length > 0) {
      const curriculumVitaeCollectionIdentifiers = curriculumVitaeCollection.map(
        curriculumVitaeItem => this.getCurriculumVitaeIdentifier(curriculumVitaeItem)!,
      );
      const curriculumVitaesToAdd = curriculumVitaes.filter(curriculumVitaeItem => {
        const curriculumVitaeIdentifier = this.getCurriculumVitaeIdentifier(curriculumVitaeItem);
        if (curriculumVitaeCollectionIdentifiers.includes(curriculumVitaeIdentifier)) {
          return false;
        }
        curriculumVitaeCollectionIdentifiers.push(curriculumVitaeIdentifier);
        return true;
      });
      return [...curriculumVitaesToAdd, ...curriculumVitaeCollection];
    }
    return curriculumVitaeCollection;
  }

  protected convertDateFromClient<T extends ICurriculumVitae | NewCurriculumVitae | PartialUpdateCurriculumVitae>(
    curriculumVitae: T,
  ): RestOf<T> {
    return {
      ...curriculumVitae,
      startDate: curriculumVitae.startDate?.format(DATE_FORMAT) ?? null,
      endDate: curriculumVitae.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCurriculumVitae: RestCurriculumVitae): ICurriculumVitae {
    return {
      ...restCurriculumVitae,
      startDate: restCurriculumVitae.startDate ? dayjs(restCurriculumVitae.startDate) : undefined,
      endDate: restCurriculumVitae.endDate ? dayjs(restCurriculumVitae.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCurriculumVitae>): HttpResponse<ICurriculumVitae> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCurriculumVitae[]>): HttpResponse<ICurriculumVitae[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
