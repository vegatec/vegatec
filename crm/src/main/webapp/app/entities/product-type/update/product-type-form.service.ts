import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProductType, NewProductType } from '../product-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductType for edit and NewProductTypeFormGroupInput for create.
 */
type ProductTypeFormGroupInput = IProductType | PartialWithRequiredKeyOf<NewProductType>;

type ProductTypeFormDefaults = Pick<NewProductType, 'id'>;

type ProductTypeFormGroupContent = {
  id: FormControl<IProductType['id'] | NewProductType['id']>;
  name: FormControl<IProductType['name']>;
  logo: FormControl<IProductType['logo']>;
  logoContentType: FormControl<IProductType['logoContentType']>;
};

export type ProductTypeFormGroup = FormGroup<ProductTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductTypeFormService {
  createProductTypeFormGroup(productType: ProductTypeFormGroupInput = { id: null }): ProductTypeFormGroup {
    const productTypeRawValue = {
      ...this.getFormDefaults(),
      ...productType,
    };
    return new FormGroup<ProductTypeFormGroupContent>({
      id: new FormControl(
        { value: productTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productTypeRawValue.name, {
        validators: [Validators.required],
      }),
      logo: new FormControl(productTypeRawValue.logo),
      logoContentType: new FormControl(productTypeRawValue.logoContentType),
    });
  }

  getProductType(form: ProductTypeFormGroup): IProductType | NewProductType {
    return form.getRawValue() as IProductType | NewProductType;
  }

  resetForm(form: ProductTypeFormGroup, productType: ProductTypeFormGroupInput): void {
    const productTypeRawValue = { ...this.getFormDefaults(), ...productType };
    form.reset(
      {
        ...productTypeRawValue,
        id: { value: productTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductTypeFormDefaults {
    return {
      id: null,
    };
  }
}
