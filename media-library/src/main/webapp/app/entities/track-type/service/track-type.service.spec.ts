import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITrackType } from '../track-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../track-type.test-samples';

import { TrackTypeService } from './track-type.service';

const requireRestSample: ITrackType = {
  ...sampleWithRequiredData,
};

describe('TrackType Service', () => {
  let service: TrackTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: ITrackType | ITrackType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TrackTypeService);
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

    it('should create a TrackType', () => {
      const trackType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(trackType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TrackType', () => {
      const trackType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(trackType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TrackType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TrackType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TrackType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a TrackType', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addTrackTypeToCollectionIfMissing', () => {
      it('should add a TrackType to an empty array', () => {
        const trackType: ITrackType = sampleWithRequiredData;
        expectedResult = service.addTrackTypeToCollectionIfMissing([], trackType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trackType);
      });

      it('should not add a TrackType to an array that contains it', () => {
        const trackType: ITrackType = sampleWithRequiredData;
        const trackTypeCollection: ITrackType[] = [
          {
            ...trackType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTrackTypeToCollectionIfMissing(trackTypeCollection, trackType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TrackType to an array that doesn't contain it", () => {
        const trackType: ITrackType = sampleWithRequiredData;
        const trackTypeCollection: ITrackType[] = [sampleWithPartialData];
        expectedResult = service.addTrackTypeToCollectionIfMissing(trackTypeCollection, trackType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trackType);
      });

      it('should add only unique TrackType to an array', () => {
        const trackTypeArray: ITrackType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const trackTypeCollection: ITrackType[] = [sampleWithRequiredData];
        expectedResult = service.addTrackTypeToCollectionIfMissing(trackTypeCollection, ...trackTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trackType: ITrackType = sampleWithRequiredData;
        const trackType2: ITrackType = sampleWithPartialData;
        expectedResult = service.addTrackTypeToCollectionIfMissing([], trackType, trackType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trackType);
        expect(expectedResult).toContain(trackType2);
      });

      it('should accept null and undefined values', () => {
        const trackType: ITrackType = sampleWithRequiredData;
        expectedResult = service.addTrackTypeToCollectionIfMissing([], null, trackType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trackType);
      });

      it('should return initial array if no TrackType is added', () => {
        const trackTypeCollection: ITrackType[] = [sampleWithRequiredData];
        expectedResult = service.addTrackTypeToCollectionIfMissing(trackTypeCollection, undefined, null);
        expect(expectedResult).toEqual(trackTypeCollection);
      });
    });

    describe('compareTrackType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTrackType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTrackType(entity1, entity2);
        const compareResult2 = service.compareTrackType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTrackType(entity1, entity2);
        const compareResult2 = service.compareTrackType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTrackType(entity1, entity2);
        const compareResult2 = service.compareTrackType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
