import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProjectMedia, NewProjectMedia } from '../project-media.model';

export type PartialUpdateProjectMedia = Partial<IProjectMedia> & Pick<IProjectMedia, 'id'>;

export type EntityResponseType = HttpResponse<IProjectMedia>;
export type EntityArrayResponseType = HttpResponse<IProjectMedia[]>;

@Injectable({ providedIn: 'root' })
export class ProjectMediaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/project-media');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(projectMedia: NewProjectMedia): Observable<EntityResponseType> {
    return this.http.post<IProjectMedia>(this.resourceUrl, projectMedia, { observe: 'response' });
  }

  update(projectMedia: IProjectMedia): Observable<EntityResponseType> {
    return this.http.put<IProjectMedia>(`${this.resourceUrl}/${this.getProjectMediaIdentifier(projectMedia)}`, projectMedia, {
      observe: 'response',
    });
  }

  partialUpdate(projectMedia: PartialUpdateProjectMedia): Observable<EntityResponseType> {
    return this.http.patch<IProjectMedia>(`${this.resourceUrl}/${this.getProjectMediaIdentifier(projectMedia)}`, projectMedia, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProjectMedia>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProjectMedia[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProjectMediaIdentifier(projectMedia: Pick<IProjectMedia, 'id'>): number {
    return projectMedia.id;
  }

  compareProjectMedia(o1: Pick<IProjectMedia, 'id'> | null, o2: Pick<IProjectMedia, 'id'> | null): boolean {
    return o1 && o2 ? this.getProjectMediaIdentifier(o1) === this.getProjectMediaIdentifier(o2) : o1 === o2;
  }

  addProjectMediaToCollectionIfMissing<Type extends Pick<IProjectMedia, 'id'>>(
    projectMediaCollection: Type[],
    ...projectMediasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const projectMedias: Type[] = projectMediasToCheck.filter(isPresent);
    if (projectMedias.length > 0) {
      const projectMediaCollectionIdentifiers = projectMediaCollection.map(
        projectMediaItem => this.getProjectMediaIdentifier(projectMediaItem)!,
      );
      const projectMediasToAdd = projectMedias.filter(projectMediaItem => {
        const projectMediaIdentifier = this.getProjectMediaIdentifier(projectMediaItem);
        if (projectMediaCollectionIdentifiers.includes(projectMediaIdentifier)) {
          return false;
        }
        projectMediaCollectionIdentifiers.push(projectMediaIdentifier);
        return true;
      });
      return [...projectMediasToAdd, ...projectMediaCollection];
    }
    return projectMediaCollection;
  }
}
