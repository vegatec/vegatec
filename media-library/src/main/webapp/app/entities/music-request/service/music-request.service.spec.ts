import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMusicRequest } from '../music-request.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../music-request.test-samples';

import { MusicRequestService, RestMusicRequest } from './music-request.service';

const requireRestSample: RestMusicRequest = {
  ...sampleWithRequiredData,
  requestedOn: sampleWithRequiredData.requestedOn?.toJSON(),
};

describe('MusicRequest Service', () => {
  let service: MusicRequestService;
  let httpMock: HttpTestingController;
  let expectedResult: IMusicRequest | IMusicRequest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MusicRequestService);
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

    it('should create a MusicRequest', () => {
      const musicRequest = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(musicRequest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MusicRequest', () => {
      const musicRequest = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(musicRequest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MusicRequest', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MusicRequest', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MusicRequest', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a MusicRequest', () => {
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

    describe('addMusicRequestToCollectionIfMissing', () => {
      it('should add a MusicRequest to an empty array', () => {
        const musicRequest: IMusicRequest = sampleWithRequiredData;
        expectedResult = service.addMusicRequestToCollectionIfMissing([], musicRequest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(musicRequest);
      });

      it('should not add a MusicRequest to an array that contains it', () => {
        const musicRequest: IMusicRequest = sampleWithRequiredData;
        const musicRequestCollection: IMusicRequest[] = [
          {
            ...musicRequest,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMusicRequestToCollectionIfMissing(musicRequestCollection, musicRequest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MusicRequest to an array that doesn't contain it", () => {
        const musicRequest: IMusicRequest = sampleWithRequiredData;
        const musicRequestCollection: IMusicRequest[] = [sampleWithPartialData];
        expectedResult = service.addMusicRequestToCollectionIfMissing(musicRequestCollection, musicRequest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(musicRequest);
      });

      it('should add only unique MusicRequest to an array', () => {
        const musicRequestArray: IMusicRequest[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const musicRequestCollection: IMusicRequest[] = [sampleWithRequiredData];
        expectedResult = service.addMusicRequestToCollectionIfMissing(musicRequestCollection, ...musicRequestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const musicRequest: IMusicRequest = sampleWithRequiredData;
        const musicRequest2: IMusicRequest = sampleWithPartialData;
        expectedResult = service.addMusicRequestToCollectionIfMissing([], musicRequest, musicRequest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(musicRequest);
        expect(expectedResult).toContain(musicRequest2);
      });

      it('should accept null and undefined values', () => {
        const musicRequest: IMusicRequest = sampleWithRequiredData;
        expectedResult = service.addMusicRequestToCollectionIfMissing([], null, musicRequest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(musicRequest);
      });

      it('should return initial array if no MusicRequest is added', () => {
        const musicRequestCollection: IMusicRequest[] = [sampleWithRequiredData];
        expectedResult = service.addMusicRequestToCollectionIfMissing(musicRequestCollection, undefined, null);
        expect(expectedResult).toEqual(musicRequestCollection);
      });
    });

    describe('compareMusicRequest', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMusicRequest(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMusicRequest(entity1, entity2);
        const compareResult2 = service.compareMusicRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMusicRequest(entity1, entity2);
        const compareResult2 = service.compareMusicRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMusicRequest(entity1, entity2);
        const compareResult2 = service.compareMusicRequest(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
