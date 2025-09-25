import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPageContentMedia } from '../page-content-media.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../page-content-media.test-samples';

import { PageContentMediaService } from './page-content-media.service';

const requireRestSample: IPageContentMedia = {
  ...sampleWithRequiredData,
};

describe('PageContentMedia Service', () => {
  let service: PageContentMediaService;
  let httpMock: HttpTestingController;
  let expectedResult: IPageContentMedia | IPageContentMedia[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PageContentMediaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PageContentMedia', () => {
      const pageContentMedia = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pageContentMedia).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PageContentMedia', () => {
      const pageContentMedia = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pageContentMedia).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PageContentMedia', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PageContentMedia', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PageContentMedia', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPageContentMediaToCollectionIfMissing', () => {
      it('should add a PageContentMedia to an empty array', () => {
        const pageContentMedia: IPageContentMedia = sampleWithRequiredData;
        expectedResult = service.addPageContentMediaToCollectionIfMissing([], pageContentMedia);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pageContentMedia);
      });

      it('should not add a PageContentMedia to an array that contains it', () => {
        const pageContentMedia: IPageContentMedia = sampleWithRequiredData;
        const pageContentMediaCollection: IPageContentMedia[] = [
          {
            ...pageContentMedia,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPageContentMediaToCollectionIfMissing(pageContentMediaCollection, pageContentMedia);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PageContentMedia to an array that doesn't contain it", () => {
        const pageContentMedia: IPageContentMedia = sampleWithRequiredData;
        const pageContentMediaCollection: IPageContentMedia[] = [sampleWithPartialData];
        expectedResult = service.addPageContentMediaToCollectionIfMissing(pageContentMediaCollection, pageContentMedia);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pageContentMedia);
      });

      it('should add only unique PageContentMedia to an array', () => {
        const pageContentMediaArray: IPageContentMedia[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pageContentMediaCollection: IPageContentMedia[] = [sampleWithRequiredData];
        expectedResult = service.addPageContentMediaToCollectionIfMissing(pageContentMediaCollection, ...pageContentMediaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pageContentMedia: IPageContentMedia = sampleWithRequiredData;
        const pageContentMedia2: IPageContentMedia = sampleWithPartialData;
        expectedResult = service.addPageContentMediaToCollectionIfMissing([], pageContentMedia, pageContentMedia2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pageContentMedia);
        expect(expectedResult).toContain(pageContentMedia2);
      });

      it('should accept null and undefined values', () => {
        const pageContentMedia: IPageContentMedia = sampleWithRequiredData;
        expectedResult = service.addPageContentMediaToCollectionIfMissing([], null, pageContentMedia, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pageContentMedia);
      });

      it('should return initial array if no PageContentMedia is added', () => {
        const pageContentMediaCollection: IPageContentMedia[] = [sampleWithRequiredData];
        expectedResult = service.addPageContentMediaToCollectionIfMissing(pageContentMediaCollection, undefined, null);
        expect(expectedResult).toEqual(pageContentMediaCollection);
      });
    });

    describe('comparePageContentMedia', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePageContentMedia(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5814 };
        const entity2 = null;

        const compareResult1 = service.comparePageContentMedia(entity1, entity2);
        const compareResult2 = service.comparePageContentMedia(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5814 };
        const entity2 = { id: 29596 };

        const compareResult1 = service.comparePageContentMedia(entity1, entity2);
        const compareResult2 = service.comparePageContentMedia(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 5814 };
        const entity2 = { id: 5814 };

        const compareResult1 = service.comparePageContentMedia(entity1, entity2);
        const compareResult2 = service.comparePageContentMedia(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
