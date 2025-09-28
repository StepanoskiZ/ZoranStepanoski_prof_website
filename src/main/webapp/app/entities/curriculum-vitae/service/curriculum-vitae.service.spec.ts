import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICurriculumVitae } from '../curriculum-vitae.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../curriculum-vitae.test-samples';

import { CurriculumVitaeService, RestCurriculumVitae } from './curriculum-vitae.service';

const requireRestSample: RestCurriculumVitae = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('CurriculumVitae Service', () => {
  let service: CurriculumVitaeService;
  let httpMock: HttpTestingController;
  let expectedResult: ICurriculumVitae | ICurriculumVitae[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CurriculumVitaeService);
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

    it('should create a CurriculumVitae', () => {
      const curriculumVitae = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(curriculumVitae).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CurriculumVitae', () => {
      const curriculumVitae = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(curriculumVitae).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CurriculumVitae', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CurriculumVitae', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CurriculumVitae', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCurriculumVitaeToCollectionIfMissing', () => {
      it('should add a CurriculumVitae to an empty array', () => {
        const curriculumVitae: ICurriculumVitae = sampleWithRequiredData;
        expectedResult = service.addCurriculumVitaeToCollectionIfMissing([], curriculumVitae);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(curriculumVitae);
      });

      it('should not add a CurriculumVitae to an array that contains it', () => {
        const curriculumVitae: ICurriculumVitae = sampleWithRequiredData;
        const curriculumVitaeCollection: ICurriculumVitae[] = [
          {
            ...curriculumVitae,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCurriculumVitaeToCollectionIfMissing(curriculumVitaeCollection, curriculumVitae);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CurriculumVitae to an array that doesn't contain it", () => {
        const curriculumVitae: ICurriculumVitae = sampleWithRequiredData;
        const curriculumVitaeCollection: ICurriculumVitae[] = [sampleWithPartialData];
        expectedResult = service.addCurriculumVitaeToCollectionIfMissing(curriculumVitaeCollection, curriculumVitae);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(curriculumVitae);
      });

      it('should add only unique CurriculumVitae to an array', () => {
        const curriculumVitaeArray: ICurriculumVitae[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const curriculumVitaeCollection: ICurriculumVitae[] = [sampleWithRequiredData];
        expectedResult = service.addCurriculumVitaeToCollectionIfMissing(curriculumVitaeCollection, ...curriculumVitaeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const curriculumVitae: ICurriculumVitae = sampleWithRequiredData;
        const curriculumVitae2: ICurriculumVitae = sampleWithPartialData;
        expectedResult = service.addCurriculumVitaeToCollectionIfMissing([], curriculumVitae, curriculumVitae2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(curriculumVitae);
        expect(expectedResult).toContain(curriculumVitae2);
      });

      it('should accept null and undefined values', () => {
        const curriculumVitae: ICurriculumVitae = sampleWithRequiredData;
        expectedResult = service.addCurriculumVitaeToCollectionIfMissing([], null, curriculumVitae, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(curriculumVitae);
      });

      it('should return initial array if no CurriculumVitae is added', () => {
        const curriculumVitaeCollection: ICurriculumVitae[] = [sampleWithRequiredData];
        expectedResult = service.addCurriculumVitaeToCollectionIfMissing(curriculumVitaeCollection, undefined, null);
        expect(expectedResult).toEqual(curriculumVitaeCollection);
      });
    });

    describe('compareCurriculumVitae', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCurriculumVitae(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCurriculumVitae(entity1, entity2);
        const compareResult2 = service.compareCurriculumVitae(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCurriculumVitae(entity1, entity2);
        const compareResult2 = service.compareCurriculumVitae(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCurriculumVitae(entity1, entity2);
        const compareResult2 = service.compareCurriculumVitae(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
