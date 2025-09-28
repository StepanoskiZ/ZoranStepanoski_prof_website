import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAboutMeMedia } from '../about-me-media.model';
import { AboutMeMediaService } from '../service/about-me-media.service';

export const aboutMeMediaResolve = (route: ActivatedRouteSnapshot): Observable<null | IAboutMeMedia> => {
  const id = route.params['id'];
  if (id) {
    return inject(AboutMeMediaService)
      .find(id)
      .pipe(
        mergeMap((aboutMeMedia: HttpResponse<IAboutMeMedia>) => {
          if (aboutMeMedia.body) {
            return of(aboutMeMedia.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default aboutMeMediaResolve;
