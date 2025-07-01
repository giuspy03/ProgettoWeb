import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private keycloakService: KeycloakService) {}

  async isLoggedIn(): Promise<boolean> {
    return this.keycloakService.isLoggedIn();
  }

  login(): void {
    this.keycloakService.login();
  }

  logout(): void {
    this.keycloakService.logout();
  }

  getUsername(): string | undefined {
    return this.keycloakService.getUsername();
  }

  getRoles(): string[] {
    return this.keycloakService.getUserRoles();
  }

  hasRole(role: string): boolean {
    return this.getRoles().includes(role);
  }
}
