import { Component, OnInit, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-layout',
  imports: [RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './layout.html',
  styleUrl: './layout.css'
})
export class Layout implements OnInit {

  esAdministrador: boolean = false;

  usuario: string = '';

  private platformId = inject(PLATFORM_ID);

  ngOnInit(): void {

    if (isPlatformBrowser(this.platformId)) {

      const usuario = sessionStorage.getItem('usuario');
      const rol = sessionStorage.getItem('rol');

      this.usuario = usuario ?? 'Usuario';

      this.esAdministrador = rol === 'ADMIN';

      console.log('Usuario:', this.usuario);
      console.log('Rol del usuario:', rol);
      console.log('¿Es administrador?:', this.esAdministrador);
    }
  }

  cerrarSesion(): void {

    sessionStorage.clear();

    window.location.href = '/login';
  }

}