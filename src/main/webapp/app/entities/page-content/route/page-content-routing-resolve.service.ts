import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPageContent } from '../page-content.model';
import { PageContentService } from '../service/page-content.service';

const pageContentResolve = (route: ActivatedRouteSnapshot): Observable<null | IPageContent> => {
  const id = route.params.id;
  if (id) {
    return inject(PageContentService)
      .find(id)
      .pipe(
        mergeMap((pageContent: HttpResponse<IPageContent>) => {
          if (pageContent.body) {
            return of(pageContent.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default pageContentResolve;
