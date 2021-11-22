import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PurchaseGameComponent } from './purchase-game.component';

describe('PurchaseGameComponent', () => {
  let component: PurchaseGameComponent;
  let fixture: ComponentFixture<PurchaseGameComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PurchaseGameComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PurchaseGameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
