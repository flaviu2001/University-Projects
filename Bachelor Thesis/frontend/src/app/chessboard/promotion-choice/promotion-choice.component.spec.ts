import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PromotionChoiceComponent } from './promotion-choice.component';

describe('PromotionChoiceComponent', () => {
  let component: PromotionChoiceComponent;
  let fixture: ComponentFixture<PromotionChoiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PromotionChoiceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PromotionChoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
