import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAuthority } from '../authority.model';

export type PartialUpdateAuthority = Partial<IAuthority> & Pick<IAuthority, 'id'>;

export type EntityResponseType = HttpResponse<IAuthority>;
export type EntityArrayResponseType = HttpResponse<IAuthority[]>;

@Injectable({ providedIn: 'root' })
export class AuthorityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/authorities');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAuthority>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAuthority[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  getAuthorityIdentifier(authority: Pick<IAuthority, 'id'>): number {
    return authority.id;
  }

  compareAuthority(o1: Pick<IAuthority, 'id'> | null, o2: Pick<IAuthority, 'id'> | null): boolean {
    return o1 && o2 ? this.getAuthorityIdentifier(o1) === this.getAuthorityIdentifier(o2) : o1 === o2;
  }

  addAuthorityToCollectionIfMissing<Type extends Pick<IAuthority, 'id'>>(
    authorityCollection: Type[],
    ...authoritiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const authorities: Type[] = authoritiesToCheck.filter(isPresent);
    if (authorities.length > 0) {
      const authorityCollectionIdentifiers = authorityCollection.map(authorityItem => this.getAuthorityIdentifier(authorityItem)!);
      const authoritiesToAdd = authorities.filter(authorityItem => {
        const authorityIdentifier = this.getAuthorityIdentifier(authorityItem);
        if (authorityCollectionIdentifiers.includes(authorityIdentifier)) {
          return false;
        }
        authorityCollectionIdentifiers.push(authorityIdentifier);
        return true;
      });
      return [...authoritiesToAdd, ...authorityCollection];
    }
    return authorityCollection;
  }
}
