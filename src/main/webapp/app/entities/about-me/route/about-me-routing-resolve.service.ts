import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAboutMe } from '../about-me.model';
import { AboutMeService } from '../service/about-me.service';

export const aboutMeResolve = (route: ActivatedRouteSnapshot): Observable<null | IAboutMe> => {
  const id = route.params['id'];
  if (id) {
    return inject(AboutMeService)
      .find(id)
      .pipe(
        mergeMap((aboutMe: HttpResponse<IAboutMe>) => {
          if (aboutMe.body) {
            return of(aboutMe.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default aboutMeResolve;
