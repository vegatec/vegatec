import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProductTypeService } from '../service/product-type.service';

import { ProductTypeComponent } from './product-type.component';

describe('ProductType Management Component', () => {
  let comp: ProductTypeComponent;
  let fixture: ComponentFixture<ProductTypeComponent>;
  let service: ProductTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'product-type', component: ProductTypeComponent }]),
        HttpClientTestingModule,
        ProductTypeComponent,
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
      .overrideTemplate(ProductTypeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductTypeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ProductTypeService);

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
    expect(comp.productTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to productTypeService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getProductTypeIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getProductTypeIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
