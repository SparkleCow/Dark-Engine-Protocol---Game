import { Routes } from '@angular/router';
import { AppComponent } from './app.component';

export const routes: Routes = [
    { path: "", component: AppComponent},
    { path: "dep", loadChildren: () => import('./features/game.routes')
        .then(m => m.gameRoutes)
    }
];
