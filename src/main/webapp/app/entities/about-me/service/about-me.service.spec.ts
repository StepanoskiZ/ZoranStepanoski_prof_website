import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAboutMe } from '../about-me.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../about-me.test-samples';

import { AboutMeService } from './about-me.service';

const requireRestSample: IAboutMe = {
  ...sampleWithRequiredData,
};

describe('AboutMe Service', () => {
  let service: AboutMeService;
  let httpMock: HttpTestingController;
  let expectedResult: IAboutMe | IAboutMe[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AboutMeService);
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

    it('should create a AboutMe', () => {
      const aboutMe = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(aboutMe).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AboutMe', () => {
      const aboutMe = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(aboutMe).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AboutMe', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AboutMe', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AboutMe', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAboutMeToCollectionIfMissing', () => {
      it('should add a AboutMe to an empty array', () => {
        const aboutMe: IAboutMe = sampleWithRequiredData;
        expectedResult = service.addAboutMeToCollectionIfMissing([], aboutMe);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aboutMe);
      });

      it('should not add a AboutMe to an array that contains it', () => {
        const aboutMe: IAboutMe = sampleWithRequiredData;
        const aboutMeCollection: IAboutMe[] = [
          {
            ...aboutMe,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAboutMeToCollectionIfMissing(aboutMeCollection, aboutMe);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AboutMe to an array that doesn't contain it", () => {
        const aboutMe: IAboutMe = sampleWithRequiredData;
        const aboutMeCollection: IAboutMe[] = [sampleWithPartialData];
        expectedResult = service.addAboutMeToCollectionIfMissing(aboutMeCollection, aboutMe);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aboutMe);
      });

      it('should add only unique AboutMe to an array', () => {
        const aboutMeArray: IAboutMe[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const aboutMeCollection: IAboutMe[] = [sampleWithRequiredData];
        expectedResult = service.addAboutMeToCollectionIfMissing(aboutMeCollection, ...aboutMeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aboutMe: IAboutMe = sampleWithRequiredData;
        const aboutMe2: IAboutMe = sampleWithPartialData;
        expectedResult = service.addAboutMeToCollectionIfMissing([], aboutMe, aboutMe2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aboutMe);
        expect(expectedResult).toContain(aboutMe2);
      });

      it('should accept null and undefined values', () => {
        const aboutMe: IAboutMe = sampleWithRequiredData;
        expectedResult = service.addAboutMeToCollectionIfMissing([], null, aboutMe, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aboutMe);
      });

      it('should return initial array if no AboutMe is added', () => {
        const aboutMeCollection: IAboutMe[] = [sampleWithRequiredData];
        expectedResult = service.addAboutMeToCollectionIfMissing(aboutMeCollection, undefined, null);
        expect(expectedResult).toEqual(aboutMeCollection);
      });
    });

    describe('compareAboutMe', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAboutMe(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAboutMe(entity1, entity2);
        const compareResult2 = service.compareAboutMe(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAboutMe(entity1, entity2);
        const compareResult2 = service.compareAboutMe(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAboutMe(entity1, entity2);
        const compareResult2 = service.compareAboutMe(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
