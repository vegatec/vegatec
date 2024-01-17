import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TrackTypeDetailComponent } from './track-type-detail.component';

describe('TrackType Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrackTypeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TrackTypeDetailComponent,
              resolve: { trackType: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TrackTypeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load trackType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TrackTypeDetailComponent);

      // THEN
      expect(instance.trackType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
