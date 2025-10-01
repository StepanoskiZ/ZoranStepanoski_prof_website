import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IBusinessServiceMedia } from '../business-service-media.model';
import { BusinessServiceMediaService } from '../service/business-service-media.service';

import businessServiceMediaResolve from './business-service-media-routing-resolve.service';

describe('BusinessServiceMedia routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: BusinessServiceMediaService;
  let resultBusinessServiceMedia: IBusinessServiceMedia | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(BusinessServiceMediaService);
    resultBusinessServiceMedia = undefined;
  });

  describe('resolve', () => {
    it('should return IBusinessServiceMedia returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        businessServiceMediaResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultBusinessServiceMedia = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBusinessServiceMedia).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        businessServiceMediaResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultBusinessServiceMedia = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultBusinessServiceMedia).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IBusinessServiceMedia>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        businessServiceMediaResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultBusinessServiceMedia = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBusinessServiceMedia).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
