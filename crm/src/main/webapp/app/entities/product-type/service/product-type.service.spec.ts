import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProductType } from '../product-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../product-type.test-samples';

import { ProductTypeService } from './product-type.service';

const requireRestSample: IProductType = {
  ...sampleWithRequiredData,
};

describe('ProductType Service', () => {
  let service: ProductTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductType | IProductType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductTypeService);
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

    it('should create a ProductType', () => {
      const productType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductType', () => {
      const productType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProductTypeToCollectionIfMissing', () => {
      it('should add a ProductType to an empty array', () => {
        const productType: IProductType = sampleWithRequiredData;
        expectedResult = service.addProductTypeToCollectionIfMissing([], productType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productType);
      });

      it('should not add a ProductType to an array that contains it', () => {
        const productType: IProductType = sampleWithRequiredData;
        const productTypeCollection: IProductType[] = [
          {
            ...productType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductTypeToCollectionIfMissing(productTypeCollection, productType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductType to an array that doesn't contain it", () => {
        const productType: IProductType = sampleWithRequiredData;
        const productTypeCollection: IProductType[] = [sampleWithPartialData];
        expectedResult = service.addProductTypeToCollectionIfMissing(productTypeCollection, productType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productType);
      });

      it('should add only unique ProductType to an array', () => {
        const productTypeArray: IProductType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productTypeCollection: IProductType[] = [sampleWithRequiredData];
        expectedResult = service.addProductTypeToCollectionIfMissing(productTypeCollection, ...productTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productType: IProductType = sampleWithRequiredData;
        const productType2: IProductType = sampleWithPartialData;
        expectedResult = service.addProductTypeToCollectionIfMissing([], productType, productType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productType);
        expect(expectedResult).toContain(productType2);
      });

      it('should accept null and undefined values', () => {
        const productType: IProductType = sampleWithRequiredData;
        expectedResult = service.addProductTypeToCollectionIfMissing([], null, productType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productType);
      });

      it('should return initial array if no ProductType is added', () => {
        const productTypeCollection: IProductType[] = [sampleWithRequiredData];
        expectedResult = service.addProductTypeToCollectionIfMissing(productTypeCollection, undefined, null);
        expect(expectedResult).toEqual(productTypeCollection);
      });
    });

    describe('compareProductType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProductType(entity1, entity2);
        const compareResult2 = service.compareProductType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProductType(entity1, entity2);
        const compareResult2 = service.compareProductType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProductType(entity1, entity2);
        const compareResult2 = service.compareProductType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
