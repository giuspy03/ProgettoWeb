import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WasherComponent } from './washer.component';

describe('Washer', () => {
  let component: WasherComponent;
  let fixture: ComponentFixture<WasherComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WasherComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WasherComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
