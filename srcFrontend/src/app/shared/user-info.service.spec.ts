import { TestBed } from '@angular/core/testing';

import { UserInfoService } from './user-info.service';

describe('UserInfo', () => {
  let service: UserInfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserInfoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
