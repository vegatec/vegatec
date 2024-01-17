import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MusicRequestDetailComponent } from './music-request-detail.component';

describe('MusicRequest Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MusicRequestDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MusicRequestDetailComponent,
              resolve: { musicRequest: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MusicRequestDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load musicRequest on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MusicRequestDetailComponent);

      // THEN
      expect(instance.musicRequest).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
