import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProductType } from 'app/entities/product-type/product-type.model';
import { ProductTypeService } from 'app/entities/product-type/service/product-type.service';
import { IBusiness } from 'app/entities/business/business.model';
import { BusinessService } from 'app/entities/business/service/business.service';
import { ProductService } from '../service/product.service';
import { IProduct } from '../product.model';
import { ProductFormService, ProductFormGroup } from './product-form.service';

@Component({
  standalone: true,
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;
  product: IProduct | null = null;

  productTypesSharedCollection: IProductType[] = [];
  productsSharedCollection: IProduct[] = [];
  businessesSharedCollection: IBusiness[] = [];

  editForm: ProductFormGroup = this.productFormService.createProductFormGroup();

  constructor(
    protected productService: ProductService,
    protected productFormService: ProductFormService,
    protected productTypeService: ProductTypeService,
    protected businessService: BusinessService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareProductType = (o1: IProductType | null, o2: IProductType | null): boolean => this.productTypeService.compareProductType(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareBusiness = (o1: IBusiness | null, o2: IBusiness | null): boolean => this.businessService.compareBusiness(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
      if (product) {
        this.updateForm(product);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.productFormService.getProduct(this.editForm);
    if (product.id !== null) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(product: IProduct): void {
    this.product = product;
    this.productFormService.resetForm(this.editForm, product);

    this.productTypesSharedCollection = this.productTypeService.addProductTypeToCollectionIfMissing<IProductType>(
      this.productTypesSharedCollection,
      product.type,
    );
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      product.componentOf,
    );
    this.businessesSharedCollection = this.businessService.addBusinessToCollectionIfMissing<IBusiness>(
      this.businessesSharedCollection,
      product.locatedAt,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productTypeService
      .query()
      .pipe(map((res: HttpResponse<IProductType[]>) => res.body ?? []))
      .pipe(
        map((productTypes: IProductType[]) =>
          this.productTypeService.addProductTypeToCollectionIfMissing<IProductType>(productTypes, this.product?.type),
        ),
      )
      .subscribe((productTypes: IProductType[]) => (this.productTypesSharedCollection = productTypes));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.product?.componentOf)),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.businessService
      .query()
      .pipe(map((res: HttpResponse<IBusiness[]>) => res.body ?? []))
      .pipe(
        map((businesses: IBusiness[]) =>
          this.businessService.addBusinessToCollectionIfMissing<IBusiness>(businesses, this.product?.locatedAt),
        ),
      )
      .subscribe((businesses: IBusiness[]) => (this.businessesSharedCollection = businesses));
  }
}
