import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICurriculumVitaeMedia } from '../curriculum-vitae-media.model';
import { CurriculumVitaeMediaService } from '../service/curriculum-vitae-media.service';

export const curriculumVitaeMediaResolve = (route: ActivatedRouteSnapshot): Observable<null | ICurriculumVitaeMedia> => {
  const id = route.params['id'];
  if (id) {
    return inject(CurriculumVitaeMediaService)
      .find(id)
      .pipe(
        mergeMap((curriculumVitaeMedia: HttpResponse<ICurriculumVitaeMedia>) => {
          if (curriculumVitaeMedia.body) {
            return of(curriculumVitaeMedia.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default curriculumVitaeMediaResolve;
