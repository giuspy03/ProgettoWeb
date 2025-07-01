import { NgModule, APP_INITIALIZER } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import {AppRoutingModule} from './app-routing-module';
import { HomeComponent } from './pages/home/home.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { AdminComponent } from './pages/dashboard/admin/admin.component';
import { WasherComponent } from './pages/dashboard/washer/washer.component';
import { UserComponent } from './pages/dashboard/user/user.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { KeycloakBearerInterceptor } from 'keycloak-angular';


@NgModule({
  declarations: [AppComponent, HomeComponent, DashboardComponent, AdminComponent, WasherComponent, UserComponent],
  imports: [BrowserModule, AppRoutingModule, KeycloakAngularModule, HttpClientModule, FormsModule],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: KeycloakBearerInterceptor,
      multi: true
    },
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService]
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}

function initializeKeycloak(keycloak: KeycloakService): () => Promise<boolean> {
  return () => keycloak.init({
    config: {
      url: 'http://localhost:8081',
      realm: 'Autolavaggio-Domicilio',
      clientId: 'frontend-client'
    },
    initOptions: {
      onLoad: 'login-required',
      checkLoginIframe: false
    }
  });
}
