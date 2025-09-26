import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVisitorLog } from '../visitor-log.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../visitor-log.test-samples';

import { VisitorLogService, RestVisitorLog } from './visitor-log.service';

const requireRestSample: RestVisitorLog = {
  ...sampleWithRequiredData,
  visitTimestamp: sampleWithRequiredData.visitTimestamp?.toJSON(),
};

describe('VisitorLog Service', () => {
  let service: VisitorLogService;
  let httpMock: HttpTestingController;
  let expectedResult: IVisitorLog | IVisitorLog[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VisitorLogService);
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

    it('should create a VisitorLog', () => {
      const visitorLog = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(visitorLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VisitorLog', () => {
      const visitorLog = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(visitorLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VisitorLog', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VisitorLog', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VisitorLog', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVisitorLogToCollectionIfMissing', () => {
      it('should add a VisitorLog to an empty array', () => {
        const visitorLog: IVisitorLog = sampleWithRequiredData;
        expectedResult = service.addVisitorLogToCollectionIfMissing([], visitorLog);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(visitorLog);
      });

      it('should not add a VisitorLog to an array that contains it', () => {
        const visitorLog: IVisitorLog = sampleWithRequiredData;
        const visitorLogCollection: IVisitorLog[] = [
          {
            ...visitorLog,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVisitorLogToCollectionIfMissing(visitorLogCollection, visitorLog);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VisitorLog to an array that doesn't contain it", () => {
        const visitorLog: IVisitorLog = sampleWithRequiredData;
        const visitorLogCollection: IVisitorLog[] = [sampleWithPartialData];
        expectedResult = service.addVisitorLogToCollectionIfMissing(visitorLogCollection, visitorLog);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(visitorLog);
      });

      it('should add only unique VisitorLog to an array', () => {
        const visitorLogArray: IVisitorLog[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const visitorLogCollection: IVisitorLog[] = [sampleWithRequiredData];
        expectedResult = service.addVisitorLogToCollectionIfMissing(visitorLogCollection, ...visitorLogArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const visitorLog: IVisitorLog = sampleWithRequiredData;
        const visitorLog2: IVisitorLog = sampleWithPartialData;
        expectedResult = service.addVisitorLogToCollectionIfMissing([], visitorLog, visitorLog2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(visitorLog);
        expect(expectedResult).toContain(visitorLog2);
      });

      it('should accept null and undefined values', () => {
        const visitorLog: IVisitorLog = sampleWithRequiredData;
        expectedResult = service.addVisitorLogToCollectionIfMissing([], null, visitorLog, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(visitorLog);
      });

      it('should return initial array if no VisitorLog is added', () => {
        const visitorLogCollection: IVisitorLog[] = [sampleWithRequiredData];
        expectedResult = service.addVisitorLogToCollectionIfMissing(visitorLogCollection, undefined, null);
        expect(expectedResult).toEqual(visitorLogCollection);
      });
    });

    describe('compareVisitorLog', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVisitorLog(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVisitorLog(entity1, entity2);
        const compareResult2 = service.compareVisitorLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVisitorLog(entity1, entity2);
        const compareResult2 = service.compareVisitorLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVisitorLog(entity1, entity2);
        const compareResult2 = service.compareVisitorLog(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
