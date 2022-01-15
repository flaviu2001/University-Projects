import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MessagesBetweenFriendsComponent } from './messages-between-friends.component';

describe('MessagesBetweenFriendsComponent', () => {
  let component: MessagesBetweenFriendsComponent;
  let fixture: ComponentFixture<MessagesBetweenFriendsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MessagesBetweenFriendsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MessagesBetweenFriendsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
