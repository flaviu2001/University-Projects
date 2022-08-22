import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChooseColorToPlayComponent } from './choose-color-to-play.component';

describe('ChooseColorToPlayComponent', () => {
  let component: ChooseColorToPlayComponent;
  let fixture: ComponentFixture<ChooseColorToPlayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChooseColorToPlayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChooseColorToPlayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
