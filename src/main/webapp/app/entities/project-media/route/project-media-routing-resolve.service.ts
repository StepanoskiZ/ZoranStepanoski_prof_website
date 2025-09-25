import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProjectMedia } from '../project-media.model';
import { ProjectMediaService } from '../service/project-media.service';

const projectMediaResolve = (route: ActivatedRouteSnapshot): Observable<null | IProjectMedia> => {
  const id = route.params.id;
  if (id) {
    return inject(ProjectMediaService)
      .find(id)
      .pipe(
        mergeMap((projectMedia: HttpResponse<IProjectMedia>) => {
          if (projectMedia.body) {
            return of(projectMedia.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default projectMediaResolve;
