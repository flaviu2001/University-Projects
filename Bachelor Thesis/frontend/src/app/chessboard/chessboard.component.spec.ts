import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChessboardComponent } from './chessboard.component';

describe('ChessboardComponent', () => {
  let component: ChessboardComponent;
  let fixture: ComponentFixture<ChessboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChessboardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChessboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
