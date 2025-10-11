import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminKeyGuard implements CanActivate {
  // Your secret key from the original nginx.conf
  private readonly ADMIN_SECRET = 'kSg8v3y_dF9aP-2hJ7rQz';

  constructor(private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    // Get the 'admin_key' from the URL's query parameters
    const providedKey = route.queryParamMap.get('admin_key');

    if (providedKey === this.ADMIN_SECRET) {
      return true; // The key is correct, allow access to the login page.
    }

    // If the key is missing or incorrect, redirect the user to the home page
    // and prevent access to the login route.
    this.router.navigate(['/']);
    return false;
  }
}
