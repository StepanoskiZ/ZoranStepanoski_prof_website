import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProjectImage } from '../project-image.model';
import { ProjectImageService } from '../service/project-image.service';

const projectImageResolve = (route: ActivatedRouteSnapshot): Observable<null | IProjectImage> => {
  const id = route.params.id;
  if (id) {
    return inject(ProjectImageService)
      .find(id)
      .pipe(
        mergeMap((projectImage: HttpResponse<IProjectImage>) => {
          if (projectImage.body) {
            return of(projectImage.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default projectImageResolve;
