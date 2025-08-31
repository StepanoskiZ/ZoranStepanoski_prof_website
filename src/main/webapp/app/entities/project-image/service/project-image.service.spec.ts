import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IProjectImage } from '../project-image.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../project-image.test-samples';

import { ProjectImageService } from './project-image.service';

const requireRestSample: IProjectImage = {
  ...sampleWithRequiredData,
};

describe('ProjectImage Service', () => {
  let service: ProjectImageService;
  let httpMock: HttpTestingController;
  let expectedResult: IProjectImage | IProjectImage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProjectImageService);
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

    it('should create a ProjectImage', () => {
      const projectImage = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(projectImage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProjectImage', () => {
      const projectImage = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(projectImage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProjectImage', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProjectImage', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProjectImage', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProjectImageToCollectionIfMissing', () => {
      it('should add a ProjectImage to an empty array', () => {
        const projectImage: IProjectImage = sampleWithRequiredData;
        expectedResult = service.addProjectImageToCollectionIfMissing([], projectImage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(projectImage);
      });

      it('should not add a ProjectImage to an array that contains it', () => {
        const projectImage: IProjectImage = sampleWithRequiredData;
        const projectImageCollection: IProjectImage[] = [
          {
            ...projectImage,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProjectImageToCollectionIfMissing(projectImageCollection, projectImage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProjectImage to an array that doesn't contain it", () => {
        const projectImage: IProjectImage = sampleWithRequiredData;
        const projectImageCollection: IProjectImage[] = [sampleWithPartialData];
        expectedResult = service.addProjectImageToCollectionIfMissing(projectImageCollection, projectImage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(projectImage);
      });

      it('should add only unique ProjectImage to an array', () => {
        const projectImageArray: IProjectImage[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const projectImageCollection: IProjectImage[] = [sampleWithRequiredData];
        expectedResult = service.addProjectImageToCollectionIfMissing(projectImageCollection, ...projectImageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const projectImage: IProjectImage = sampleWithRequiredData;
        const projectImage2: IProjectImage = sampleWithPartialData;
        expectedResult = service.addProjectImageToCollectionIfMissing([], projectImage, projectImage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(projectImage);
        expect(expectedResult).toContain(projectImage2);
      });

      it('should accept null and undefined values', () => {
        const projectImage: IProjectImage = sampleWithRequiredData;
        expectedResult = service.addProjectImageToCollectionIfMissing([], null, projectImage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(projectImage);
      });

      it('should return initial array if no ProjectImage is added', () => {
        const projectImageCollection: IProjectImage[] = [sampleWithRequiredData];
        expectedResult = service.addProjectImageToCollectionIfMissing(projectImageCollection, undefined, null);
        expect(expectedResult).toEqual(projectImageCollection);
      });
    });

    describe('compareProjectImage', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProjectImage(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 30515 };
        const entity2 = null;

        const compareResult1 = service.compareProjectImage(entity1, entity2);
        const compareResult2 = service.compareProjectImage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 30515 };
        const entity2 = { id: 22654 };

        const compareResult1 = service.compareProjectImage(entity1, entity2);
        const compareResult2 = service.compareProjectImage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 30515 };
        const entity2 = { id: 30515 };

        const compareResult1 = service.compareProjectImage(entity1, entity2);
        const compareResult2 = service.compareProjectImage(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
