import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPageContent } from '../page-content.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../page-content.test-samples';

import { PageContentService } from './page-content.service';

const requireRestSample: IPageContent = {
  ...sampleWithRequiredData,
};

describe('PageContent Service', () => {
  let service: PageContentService;
  let httpMock: HttpTestingController;
  let expectedResult: IPageContent | IPageContent[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PageContentService);
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

    it('should create a PageContent', () => {
      const pageContent = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pageContent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PageContent', () => {
      const pageContent = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pageContent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PageContent', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PageContent', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PageContent', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPageContentToCollectionIfMissing', () => {
      it('should add a PageContent to an empty array', () => {
        const pageContent: IPageContent = sampleWithRequiredData;
        expectedResult = service.addPageContentToCollectionIfMissing([], pageContent);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pageContent);
      });

      it('should not add a PageContent to an array that contains it', () => {
        const pageContent: IPageContent = sampleWithRequiredData;
        const pageContentCollection: IPageContent[] = [
          {
            ...pageContent,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPageContentToCollectionIfMissing(pageContentCollection, pageContent);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PageContent to an array that doesn't contain it", () => {
        const pageContent: IPageContent = sampleWithRequiredData;
        const pageContentCollection: IPageContent[] = [sampleWithPartialData];
        expectedResult = service.addPageContentToCollectionIfMissing(pageContentCollection, pageContent);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pageContent);
      });

      it('should add only unique PageContent to an array', () => {
        const pageContentArray: IPageContent[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pageContentCollection: IPageContent[] = [sampleWithRequiredData];
        expectedResult = service.addPageContentToCollectionIfMissing(pageContentCollection, ...pageContentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pageContent: IPageContent = sampleWithRequiredData;
        const pageContent2: IPageContent = sampleWithPartialData;
        expectedResult = service.addPageContentToCollectionIfMissing([], pageContent, pageContent2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pageContent);
        expect(expectedResult).toContain(pageContent2);
      });

      it('should accept null and undefined values', () => {
        const pageContent: IPageContent = sampleWithRequiredData;
        expectedResult = service.addPageContentToCollectionIfMissing([], null, pageContent, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pageContent);
      });

      it('should return initial array if no PageContent is added', () => {
        const pageContentCollection: IPageContent[] = [sampleWithRequiredData];
        expectedResult = service.addPageContentToCollectionIfMissing(pageContentCollection, undefined, null);
        expect(expectedResult).toEqual(pageContentCollection);
      });
    });

    describe('comparePageContent', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePageContent(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePageContent(entity1, entity2);
        const compareResult2 = service.comparePageContent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePageContent(entity1, entity2);
        const compareResult2 = service.comparePageContent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePageContent(entity1, entity2);
        const compareResult2 = service.comparePageContent(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
