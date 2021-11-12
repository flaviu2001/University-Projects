import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StorePageComponent } from './store-page.component';

describe('StorePageComponent', () => {
  let component: StorePageComponent;
  let fixture: ComponentFixture<StorePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StorePageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StorePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
