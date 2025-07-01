import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})

export class HomeComponent {
  constructor(private router: Router, private keycloakService: KeycloakService) {}

  async vaiAllaDashboard() {
    const userRoles = this.keycloakService.getUserRoles();

    if (userRoles.includes('admin')) {
      this.router.navigate(['/dashboard/admin']);
    } else if (userRoles.includes('washer')) {
      this.router.navigate(['/dashboard/washer']);
    } else {
      this.router.navigate(['/dashboard/user']);
    }
  }
  logout() {
    this.keycloakService.logout();
  }
}

