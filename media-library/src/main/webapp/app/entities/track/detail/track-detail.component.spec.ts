import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TrackDetailComponent } from './track-detail.component';

describe('Track Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrackDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TrackDetailComponent,
              resolve: { track: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TrackDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load track on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TrackDetailComponent);

      // THEN
      expect(instance.track).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
