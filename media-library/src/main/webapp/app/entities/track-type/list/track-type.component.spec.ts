import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TrackTypeService } from '../service/track-type.service';

import { TrackTypeComponent } from './track-type.component';

describe('TrackType Management Component', () => {
  let comp: TrackTypeComponent;
  let fixture: ComponentFixture<TrackTypeComponent>;
  let service: TrackTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'track-type', component: TrackTypeComponent }]),
        HttpClientTestingModule,
        TrackTypeComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(TrackTypeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrackTypeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TrackTypeService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.trackTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to trackTypeService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getTrackTypeIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getTrackTypeIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
