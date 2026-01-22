import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { MenuComponent } from './features/menu/menu/menu.component';

export const routes: Routes = [
  { path: '', component: AppComponent },
  {
    path: 'dep',
    loadChildren: () =>
      import('./features/game.routes').then((m) => m.gameRoutes),
  },
  { path: 'menu', component: MenuComponent },
];
