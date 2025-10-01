import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVisitorLog, NewVisitorLog } from '../visitor-log.model';

export type PartialUpdateVisitorLog = Partial<IVisitorLog> & Pick<IVisitorLog, 'id'>;

type RestOf<T extends IVisitorLog | NewVisitorLog> = Omit<T, 'visitTimestamp'> & {
  visitTimestamp?: string | null;
};

export type RestVisitorLog = RestOf<IVisitorLog>;

export type NewRestVisitorLog = RestOf<NewVisitorLog>;

export type PartialUpdateRestVisitorLog = RestOf<PartialUpdateVisitorLog>;

export type EntityResponseType = HttpResponse<IVisitorLog>;
export type EntityArrayResponseType = HttpResponse<IVisitorLog[]>;

@Injectable({ providedIn: 'root' })
export class VisitorLogService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/visitor-log');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(visitorLog: NewVisitorLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(visitorLog);
    return this.http
      .post<RestVisitorLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(visitorLog: IVisitorLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(visitorLog);
    return this.http
      .put<RestVisitorLog>(`${this.resourceUrl}/${this.getVisitorLogIdentifier(visitorLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(visitorLog: PartialUpdateVisitorLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(visitorLog);
    return this.http
      .patch<RestVisitorLog>(`${this.resourceUrl}/${this.getVisitorLogIdentifier(visitorLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestVisitorLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVisitorLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVisitorLogIdentifier(visitorLog: Pick<IVisitorLog, 'id'>): number {
    return visitorLog.id;
  }

  compareVisitorLog(o1: Pick<IVisitorLog, 'id'> | null, o2: Pick<IVisitorLog, 'id'> | null): boolean {
    return o1 && o2 ? this.getVisitorLogIdentifier(o1) === this.getVisitorLogIdentifier(o2) : o1 === o2;
  }

  addVisitorLogToCollectionIfMissing<Type extends Pick<IVisitorLog, 'id'>>(
    visitorLogCollection: Type[],
    ...visitorLogsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const visitorLogs: Type[] = visitorLogsToCheck.filter(isPresent);
    if (visitorLogs.length > 0) {
      const visitorLogCollectionIdentifiers = visitorLogCollection.map(visitorLogItem => this.getVisitorLogIdentifier(visitorLogItem)!);
      const visitorLogsToAdd = visitorLogs.filter(visitorLogItem => {
        const visitorLogIdentifier = this.getVisitorLogIdentifier(visitorLogItem);
        if (visitorLogCollectionIdentifiers.includes(visitorLogIdentifier)) {
          return false;
        }
        visitorLogCollectionIdentifiers.push(visitorLogIdentifier);
        return true;
      });
      return [...visitorLogsToAdd, ...visitorLogCollection];
    }
    return visitorLogCollection;
  }

  protected convertDateFromClient<T extends IVisitorLog | NewVisitorLog | PartialUpdateVisitorLog>(visitorLog: T): RestOf<T> {
    return {
      ...visitorLog,
      visitTimestamp: visitorLog.visitTimestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restVisitorLog: RestVisitorLog): IVisitorLog {
    return {
      ...restVisitorLog,
      visitTimestamp: restVisitorLog.visitTimestamp ? dayjs(restVisitorLog.visitTimestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVisitorLog>): HttpResponse<IVisitorLog> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVisitorLog[]>): HttpResponse<IVisitorLog[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
