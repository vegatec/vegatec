import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProduct, NewProduct } from '../product.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProduct for edit and NewProductFormGroupInput for create.
 */
type ProductFormGroupInput = IProduct | PartialWithRequiredKeyOf<NewProduct>;

type ProductFormDefaults = Pick<NewProduct, 'id'>;

type ProductFormGroupContent = {
  id: FormControl<IProduct['id'] | NewProduct['id']>;
  name: FormControl<IProduct['name']>;
  model: FormControl<IProduct['model']>;
  serialNumber: FormControl<IProduct['serialNumber']>;
  manufacturer: FormControl<IProduct['manufacturer']>;
  createdOn: FormControl<IProduct['createdOn']>;
  type: FormControl<IProduct['type']>;
  locatedAt: FormControl<IProduct['locatedAt']>;
  componentOf: FormControl<IProduct['componentOf']>;
};

export type ProductFormGroup = FormGroup<ProductFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductFormService {
  createProductFormGroup(product: ProductFormGroupInput = { id: null }): ProductFormGroup {
    const productRawValue = {
      ...this.getFormDefaults(),
      ...product,
    };
    return new FormGroup<ProductFormGroupContent>({
      id: new FormControl(
        { value: productRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productRawValue.name, {
        validators: [Validators.required],
      }),
      model: new FormControl(productRawValue.model, {
        validators: [Validators.required],
      }),
      serialNumber: new FormControl(productRawValue.serialNumber, {
        validators: [Validators.required],
      }),
      manufacturer: new FormControl(productRawValue.manufacturer),
      createdOn: new FormControl(productRawValue.createdOn, {
        validators: [Validators.required],
      }),
      type: new FormControl(productRawValue.type),
      locatedAt: new FormControl(productRawValue.locatedAt),
      componentOf: new FormControl(productRawValue.componentOf),
    });
  }

  getProduct(form: ProductFormGroup): IProduct | NewProduct {
    return form.getRawValue() as IProduct | NewProduct;
  }

  resetForm(form: ProductFormGroup, product: ProductFormGroupInput): void {
    const productRawValue = { ...this.getFormDefaults(), ...product };
    form.reset(
      {
        ...productRawValue,
        id: { value: productRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductFormDefaults {
    return {
      id: null,
    };
  }
}
