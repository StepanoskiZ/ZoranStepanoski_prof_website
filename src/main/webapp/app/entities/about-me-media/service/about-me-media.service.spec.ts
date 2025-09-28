import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAboutMeMedia } from '../about-me-media.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../about-me-media.test-samples';

import { AboutMeMediaService } from './about-me-media.service';

const requireRestSample: IAboutMeMedia = {
  ...sampleWithRequiredData,
};

describe('AboutMeMedia Service', () => {
  let service: AboutMeMediaService;
  let httpMock: HttpTestingController;
  let expectedResult: IAboutMeMedia | IAboutMeMedia[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AboutMeMediaService);
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

    it('should create a AboutMeMedia', () => {
      const aboutMeMedia = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(aboutMeMedia).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AboutMeMedia', () => {
      const aboutMeMedia = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(aboutMeMedia).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AboutMeMedia', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AboutMeMedia', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AboutMeMedia', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAboutMeMediaToCollectionIfMissing', () => {
      it('should add a AboutMeMedia to an empty array', () => {
        const aboutMeMedia: IAboutMeMedia = sampleWithRequiredData;
        expectedResult = service.addAboutMeMediaToCollectionIfMissing([], aboutMeMedia);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aboutMeMedia);
      });

      it('should not add a AboutMeMedia to an array that contains it', () => {
        const aboutMeMedia: IAboutMeMedia = sampleWithRequiredData;
        const aboutMeMediaCollection: IAboutMeMedia[] = [
          {
            ...aboutMeMedia,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAboutMeMediaToCollectionIfMissing(aboutMeMediaCollection, aboutMeMedia);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AboutMeMedia to an array that doesn't contain it", () => {
        const aboutMeMedia: IAboutMeMedia = sampleWithRequiredData;
        const aboutMeMediaCollection: IAboutMeMedia[] = [sampleWithPartialData];
        expectedResult = service.addAboutMeMediaToCollectionIfMissing(aboutMeMediaCollection, aboutMeMedia);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aboutMeMedia);
      });

      it('should add only unique AboutMeMedia to an array', () => {
        const aboutMeMediaArray: IAboutMeMedia[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const aboutMeMediaCollection: IAboutMeMedia[] = [sampleWithRequiredData];
        expectedResult = service.addAboutMeMediaToCollectionIfMissing(aboutMeMediaCollection, ...aboutMeMediaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aboutMeMedia: IAboutMeMedia = sampleWithRequiredData;
        const aboutMeMedia2: IAboutMeMedia = sampleWithPartialData;
        expectedResult = service.addAboutMeMediaToCollectionIfMissing([], aboutMeMedia, aboutMeMedia2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aboutMeMedia);
        expect(expectedResult).toContain(aboutMeMedia2);
      });

      it('should accept null and undefined values', () => {
        const aboutMeMedia: IAboutMeMedia = sampleWithRequiredData;
        expectedResult = service.addAboutMeMediaToCollectionIfMissing([], null, aboutMeMedia, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aboutMeMedia);
      });

      it('should return initial array if no AboutMeMedia is added', () => {
        const aboutMeMediaCollection: IAboutMeMedia[] = [sampleWithRequiredData];
        expectedResult = service.addAboutMeMediaToCollectionIfMissing(aboutMeMediaCollection, undefined, null);
        expect(expectedResult).toEqual(aboutMeMediaCollection);
      });
    });

    describe('compareAboutMeMedia', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAboutMeMedia(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAboutMeMedia(entity1, entity2);
        const compareResult2 = service.compareAboutMeMedia(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAboutMeMedia(entity1, entity2);
        const compareResult2 = service.compareAboutMeMedia(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAboutMeMedia(entity1, entity2);
        const compareResult2 = service.compareAboutMeMedia(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
