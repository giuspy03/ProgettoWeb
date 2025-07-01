import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { AuthGuard } from './auth/auth-guard';
import { RoleGuard } from './auth/role-guard';

import { AdminComponent } from './pages/dashboard/admin/admin.component';
import { WasherComponent } from './pages/dashboard/washer/washer.component';
import { UserComponent } from './pages/dashboard/user/user.component';

const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuard] },
  {
    path: 'dashboard/admin',
    component: AdminComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['admin'] }
  },
  {
    path: 'dashboard/washer',
    component: WasherComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['washer'] }
  },
  {
    path: 'dashboard/user',
    component: UserComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['user'] }
  },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
