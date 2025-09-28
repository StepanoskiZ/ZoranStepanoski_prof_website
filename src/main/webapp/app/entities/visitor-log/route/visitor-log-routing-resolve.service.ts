import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVisitorLog } from '../visitor-log.model';
import { VisitorLogService } from '../service/visitor-log.service';

export const visitorLogResolve = (route: ActivatedRouteSnapshot): Observable<null | IVisitorLog> => {
  const id = route.params['id'];
  if (id) {
    return inject(VisitorLogService)
      .find(id)
      .pipe(
        mergeMap((visitorLog: HttpResponse<IVisitorLog>) => {
          if (visitorLog.body) {
            return of(visitorLog.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default visitorLogResolve;
