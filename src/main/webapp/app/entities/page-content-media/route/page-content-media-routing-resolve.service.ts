import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPageContentMedia } from '../page-content-media.model';
import { PageContentMediaService } from '../service/page-content-media.service';

export const pageContentMediaResolve = (route: ActivatedRouteSnapshot): Observable<null | IPageContentMedia> => {
  const id = route.params['id'];
  if (id) {
    return inject(PageContentMediaService)
      .find(id)
      .pipe(
        mergeMap((pageContentMedia: HttpResponse<IPageContentMedia>) => {
          if (pageContentMedia.body) {
            return of(pageContentMedia.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default pageContentMediaResolve;
