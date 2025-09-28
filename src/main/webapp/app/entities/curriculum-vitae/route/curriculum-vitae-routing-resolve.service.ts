import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICurriculumVitae } from '../curriculum-vitae.model';
import { CurriculumVitaeService } from '../service/curriculum-vitae.service';

export const curriculumVitaeResolve = (route: ActivatedRouteSnapshot): Observable<null | ICurriculumVitae> => {
  const id = route.params['id'];
  if (id) {
    return inject(CurriculumVitaeService)
      .find(id)
      .pipe(
        mergeMap((curriculumVitae: HttpResponse<ICurriculumVitae>) => {
          if (curriculumVitae.body) {
            return of(curriculumVitae.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default curriculumVitaeResolve;
