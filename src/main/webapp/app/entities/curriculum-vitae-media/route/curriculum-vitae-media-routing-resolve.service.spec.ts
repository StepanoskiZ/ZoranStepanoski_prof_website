import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICurriculumVitaeMedia } from '../curriculum-vitae-media.model';
import { CurriculumVitaeMediaService } from '../service/curriculum-vitae-media.service';

import curriculumVitaeMediaResolve from './curriculum-vitae-media-routing-resolve.service';

describe('CurriculumVitaeMedia routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: CurriculumVitaeMediaService;
  let resultCurriculumVitaeMedia: ICurriculumVitaeMedia | null | undefined;

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
    service = TestBed.inject(CurriculumVitaeMediaService);
    resultCurriculumVitaeMedia = undefined;
  });

  describe('resolve', () => {
    it('should return ICurriculumVitaeMedia returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        curriculumVitaeMediaResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultCurriculumVitaeMedia = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCurriculumVitaeMedia).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        curriculumVitaeMediaResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultCurriculumVitaeMedia = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCurriculumVitaeMedia).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ICurriculumVitaeMedia>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        curriculumVitaeMediaResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultCurriculumVitaeMedia = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCurriculumVitaeMedia).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
