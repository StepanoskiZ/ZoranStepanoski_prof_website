import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProjectMedia } from '../project-media.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../project-media.test-samples';

import { ProjectMediaService } from './project-media.service';

const requireRestSample: IProjectMedia = {
  ...sampleWithRequiredData,
};

describe('ProjectMedia Service', () => {
  let service: ProjectMediaService;
  let httpMock: HttpTestingController;
  let expectedResult: IProjectMedia | IProjectMedia[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProjectMediaService);
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

    it('should create a ProjectMedia', () => {
      const projectMedia = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(projectMedia).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProjectMedia', () => {
      const projectMedia = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(projectMedia).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProjectMedia', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProjectMedia', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProjectMedia', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProjectMediaToCollectionIfMissing', () => {
      it('should add a ProjectMedia to an empty array', () => {
        const projectMedia: IProjectMedia = sampleWithRequiredData;
        expectedResult = service.addProjectMediaToCollectionIfMissing([], projectMedia);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(projectMedia);
      });

      it('should not add a ProjectMedia to an array that contains it', () => {
        const projectMedia: IProjectMedia = sampleWithRequiredData;
        const projectMediaCollection: IProjectMedia[] = [
          {
            ...projectMedia,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProjectMediaToCollectionIfMissing(projectMediaCollection, projectMedia);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProjectMedia to an array that doesn't contain it", () => {
        const projectMedia: IProjectMedia = sampleWithRequiredData;
        const projectMediaCollection: IProjectMedia[] = [sampleWithPartialData];
        expectedResult = service.addProjectMediaToCollectionIfMissing(projectMediaCollection, projectMedia);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(projectMedia);
      });

      it('should add only unique ProjectMedia to an array', () => {
        const projectMediaArray: IProjectMedia[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const projectMediaCollection: IProjectMedia[] = [sampleWithRequiredData];
        expectedResult = service.addProjectMediaToCollectionIfMissing(projectMediaCollection, ...projectMediaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const projectMedia: IProjectMedia = sampleWithRequiredData;
        const projectMedia2: IProjectMedia = sampleWithPartialData;
        expectedResult = service.addProjectMediaToCollectionIfMissing([], projectMedia, projectMedia2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(projectMedia);
        expect(expectedResult).toContain(projectMedia2);
      });

      it('should accept null and undefined values', () => {
        const projectMedia: IProjectMedia = sampleWithRequiredData;
        expectedResult = service.addProjectMediaToCollectionIfMissing([], null, projectMedia, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(projectMedia);
      });

      it('should return initial array if no ProjectMedia is added', () => {
        const projectMediaCollection: IProjectMedia[] = [sampleWithRequiredData];
        expectedResult = service.addProjectMediaToCollectionIfMissing(projectMediaCollection, undefined, null);
        expect(expectedResult).toEqual(projectMediaCollection);
      });
    });

    describe('compareProjectMedia', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProjectMedia(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProjectMedia(entity1, entity2);
        const compareResult2 = service.compareProjectMedia(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProjectMedia(entity1, entity2);
        const compareResult2 = service.compareProjectMedia(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProjectMedia(entity1, entity2);
        const compareResult2 = service.compareProjectMedia(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
