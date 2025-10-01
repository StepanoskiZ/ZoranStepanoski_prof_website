import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBusinessServiceMedia } from '../business-service-media.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../business-service-media.test-samples';

import { BusinessServiceMediaService } from './business-service-media.service';

const requireRestSample: IBusinessServiceMedia = {
  ...sampleWithRequiredData,
};

describe('BusinessServiceMedia Service', () => {
  let service: BusinessServiceMediaService;
  let httpMock: HttpTestingController;
  let expectedResult: IBusinessServiceMedia | IBusinessServiceMedia[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BusinessServiceMediaService);
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

    it('should create a BusinessServiceMedia', () => {
      const businessServiceMedia = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(businessServiceMedia).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BusinessServiceMedia', () => {
      const businessServiceMedia = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(businessServiceMedia).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BusinessServiceMedia', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BusinessServiceMedia', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BusinessServiceMedia', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBusinessServiceMediaToCollectionIfMissing', () => {
      it('should add a BusinessServiceMedia to an empty array', () => {
        const businessServiceMedia: IBusinessServiceMedia = sampleWithRequiredData;
        expectedResult = service.addBusinessServiceMediaToCollectionIfMissing([], businessServiceMedia);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(businessServiceMedia);
      });

      it('should not add a BusinessServiceMedia to an array that contains it', () => {
        const businessServiceMedia: IBusinessServiceMedia = sampleWithRequiredData;
        const businessServiceMediaCollection: IBusinessServiceMedia[] = [
          {
            ...businessServiceMedia,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBusinessServiceMediaToCollectionIfMissing(businessServiceMediaCollection, businessServiceMedia);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BusinessServiceMedia to an array that doesn't contain it", () => {
        const businessServiceMedia: IBusinessServiceMedia = sampleWithRequiredData;
        const businessServiceMediaCollection: IBusinessServiceMedia[] = [sampleWithPartialData];
        expectedResult = service.addBusinessServiceMediaToCollectionIfMissing(businessServiceMediaCollection, businessServiceMedia);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(businessServiceMedia);
      });

      it('should add only unique BusinessServiceMedia to an array', () => {
        const businessServiceMediaArray: IBusinessServiceMedia[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const businessServiceMediaCollection: IBusinessServiceMedia[] = [sampleWithRequiredData];
        expectedResult = service.addBusinessServiceMediaToCollectionIfMissing(businessServiceMediaCollection, ...businessServiceMediaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const businessServiceMedia: IBusinessServiceMedia = sampleWithRequiredData;
        const businessServiceMedia2: IBusinessServiceMedia = sampleWithPartialData;
        expectedResult = service.addBusinessServiceMediaToCollectionIfMissing([], businessServiceMedia, businessServiceMedia2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(businessServiceMedia);
        expect(expectedResult).toContain(businessServiceMedia2);
      });

      it('should accept null and undefined values', () => {
        const businessServiceMedia: IBusinessServiceMedia = sampleWithRequiredData;
        expectedResult = service.addBusinessServiceMediaToCollectionIfMissing([], null, businessServiceMedia, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(businessServiceMedia);
      });

      it('should return initial array if no BusinessServiceMedia is added', () => {
        const businessServiceMediaCollection: IBusinessServiceMedia[] = [sampleWithRequiredData];
        expectedResult = service.addBusinessServiceMediaToCollectionIfMissing(businessServiceMediaCollection, undefined, null);
        expect(expectedResult).toEqual(businessServiceMediaCollection);
      });
    });

    describe('compareBusinessServiceMedia', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBusinessServiceMedia(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBusinessServiceMedia(entity1, entity2);
        const compareResult2 = service.compareBusinessServiceMedia(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBusinessServiceMedia(entity1, entity2);
        const compareResult2 = service.compareBusinessServiceMedia(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBusinessServiceMedia(entity1, entity2);
        const compareResult2 = service.compareBusinessServiceMedia(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
