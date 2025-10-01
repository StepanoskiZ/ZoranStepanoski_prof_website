import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBusinessServiceMedia } from '../business-service-media.model';
import { BusinessServiceMediaService } from '../service/business-service-media.service';

export const businessServiceMediaResolve = (route: ActivatedRouteSnapshot): Observable<null | IBusinessServiceMedia> => {
  const id = route.params['id'];
  if (id) {
    return inject(BusinessServiceMediaService)
      .find(id)
      .pipe(
        mergeMap((businessServiceMedia: HttpResponse<IBusinessServiceMedia>) => {
          if (businessServiceMedia.body) {
            return of(businessServiceMedia.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default businessServiceMediaResolve;
