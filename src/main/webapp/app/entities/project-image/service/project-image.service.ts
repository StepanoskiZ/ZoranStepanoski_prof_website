import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProjectImage, NewProjectImage } from '../project-image.model';

export type PartialUpdateProjectImage = Partial<IProjectImage> & Pick<IProjectImage, 'id'>;

export type EntityResponseType = HttpResponse<IProjectImage>;
export type EntityArrayResponseType = HttpResponse<IProjectImage[]>;

@Injectable({ providedIn: 'root' })
export class ProjectImageService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/project-images');

  create(projectImage: NewProjectImage): Observable<EntityResponseType> {
    return this.http.post<IProjectImage>(this.resourceUrl, projectImage, { observe: 'response' });
  }

  update(projectImage: IProjectImage): Observable<EntityResponseType> {
    return this.http.put<IProjectImage>(`${this.resourceUrl}/${this.getProjectImageIdentifier(projectImage)}`, projectImage, {
      observe: 'response',
    });
  }

  partialUpdate(projectImage: PartialUpdateProjectImage): Observable<EntityResponseType> {
    return this.http.patch<IProjectImage>(`${this.resourceUrl}/${this.getProjectImageIdentifier(projectImage)}`, projectImage, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProjectImage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProjectImage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProjectImageIdentifier(projectImage: Pick<IProjectImage, 'id'>): number {
    return projectImage.id;
  }

  compareProjectImage(o1: Pick<IProjectImage, 'id'> | null, o2: Pick<IProjectImage, 'id'> | null): boolean {
    return o1 && o2 ? this.getProjectImageIdentifier(o1) === this.getProjectImageIdentifier(o2) : o1 === o2;
  }

  addProjectImageToCollectionIfMissing<Type extends Pick<IProjectImage, 'id'>>(
    projectImageCollection: Type[],
    ...projectImagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const projectImages: Type[] = projectImagesToCheck.filter(isPresent);
    if (projectImages.length > 0) {
      const projectImageCollectionIdentifiers = projectImageCollection.map(projectImageItem =>
        this.getProjectImageIdentifier(projectImageItem),
      );
      const projectImagesToAdd = projectImages.filter(projectImageItem => {
        const projectImageIdentifier = this.getProjectImageIdentifier(projectImageItem);
        if (projectImageCollectionIdentifiers.includes(projectImageIdentifier)) {
          return false;
        }
        projectImageCollectionIdentifiers.push(projectImageIdentifier);
        return true;
      });
      return [...projectImagesToAdd, ...projectImageCollection];
    }
    return projectImageCollection;
  }
}
