import { Routes } from '@angular/router';

import { Login } from './componentes/login/login';
import { Layout } from './componentes/layout/layout';
import { Dashboard } from './componentes/dashboard/dashboard';
import { Preventa } from './componentes/preventa/preventa';
import { Productos } from './componentes/productos/productos';
import { Cotizacion } from './componentes/cotizacion/cotizacion';
import { ConsultarPreventas } from './componentes/consultar-preventas/consultar-preventas';
import { DetallePreventa } from './componentes/detalle-preventa/detalle-preventa';
import { DetalleCotizacion } from './componentes/detalle-cotizacion/detalle-cotizacion';
import { ConsultarCotizaciones } from './componentes/consultar-cotizaciones/consultar-cotizaciones';
import { Inicio } from './componentes/inicio/inicio';
import { authGuard } from './guards/auth.guard';
import { Clientes } from './componentes/clientes/clientes';
import { Documentos } from './componentes/documentos/documentos';
import { Usuarios } from './componentes/usuarios/usuarios';
import { Administrador } from './componentes/administrador/administrador';
import { adminGuard } from './guards/admin-guard';

export const routes: Routes = [

  // LOGIN
  {
    path: 'login',
    component: Login
  },

  // SISTEMA PRINCIPAL
  {
    path: '',
    component: Layout,
    canActivate: [authGuard],

    children: [
      { path: 'inicio', component: Inicio },
      { path: 'dashboard', component: Dashboard },
      {path:'clientes', component:Clientes},
      { path: 'preventa', component: Preventa },
      { path: 'productos', component: Productos },
      { path: 'cotizacion/:id', component: Cotizacion },
      { path: 'consultar-preventas', component: ConsultarPreventas },
      { path: 'detalle-preventa/:id', component: DetallePreventa },
      { path: 'detalle-cotizacion/:id', component: DetalleCotizacion },
      { path: 'consultar-cotizaciones', component: ConsultarCotizaciones },
      { path:'documentos',component:Documentos},
      { path:'usuarios',component:Usuarios},
      { path: 'administrador', component: Administrador,canActivate: [adminGuard]
}
    ]
  },

  // Ruta inicial
  {
    path: '**',
    redirectTo: 'dashboard'
  }

];