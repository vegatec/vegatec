import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UrlReceiverComponent } from './url-receiver.component';

describe('UrlReceiverComponent', () => {
  let component: UrlReceiverComponent;
  let fixture: ComponentFixture<UrlReceiverComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UrlReceiverComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UrlReceiverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
