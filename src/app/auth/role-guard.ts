import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree} from '@angular/router';
import {AuthService} from './auth-service';


@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(private auth: AuthService) {}

  async canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Promise<boolean | UrlTree> {
    const requiredRoles = route.data['roles'] as string[];
    const isLoggedIn = await this.auth.isLoggedIn();

    if (!isLoggedIn) {
      this.auth.login();
      return false;
    }

    return requiredRoles.some(role => this.auth.hasRole(role));
  }
}
