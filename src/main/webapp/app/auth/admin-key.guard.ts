import { CanActivateFn } from '@angular/router';

export const adminKeyGuard: CanActivateFn = (route, state) => {
  return true;
};
