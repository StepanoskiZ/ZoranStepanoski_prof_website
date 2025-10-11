import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { adminKeyGuard } from './admin-key.guard';

describe('adminKeyGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => adminKeyGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
