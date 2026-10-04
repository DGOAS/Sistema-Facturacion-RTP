import { Component, OnInit, ChangeDetectorRef, afterNextRender } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface UsuarioPerfil {
  idUsuario?: number;
  usuario: string;
  nombres: string;
  apellidos: string;
  dni: string;
  telefono: string;
  rol: string;
}

@Component({
  selector: 'app-usuarios',
  imports: [FormsModule],
  templateUrl: './usuarios.html',
  styleUrl: './usuarios.css'
})
export class Usuarios implements OnInit {

  perfil: UsuarioPerfil = {
    usuario: '',
    nombres: '',
    apellidos: '',
    dni: '',
    telefono: '',
    rol: ''
  };

  nuevaPassword: string = '';
  confirmarPassword: string = '';

  cargando: boolean = true;
  guardando: boolean = false;
  cambiandoPassword: boolean = false;

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {

    afterNextRender(() => {
      this.cargarPerfil();
    });

  }

  ngOnInit(): void {
    // La carga del perfil se realiza en afterNextRender() para evitar problemas con SSR y sessionStorage.
  }

  // CARGAR PERFIL


  cargarPerfil(): void {

    const usuario = sessionStorage.getItem('usuario');

    console.log('Usuario de la sesión:', usuario);

    if (!usuario) {
      this.cargando = false;
      console.error('No se encontró el usuario en sessionStorage.');
      return;
    }

    this.http
      .get<UsuarioPerfil>(
        `http://localhost:8080/api/usuarios/${usuario}`
      )
      .subscribe({

        next: (respuesta) => {

          console.log('Perfil recibido:', respuesta);

          this.perfil = respuesta;
          this.cargando = false;

          this.cdr.detectChanges();
        },

        error: (error) => {

          console.error(
            'Error al cargar el perfil:',
            error
          );

          this.cargando = false;

          alert(
            'No se pudo cargar la información del usuario.'
          );
        }

      });
  }

  // ACTUALIZAR PERFIL


  actualizarPerfil(): void {

    const usuario = sessionStorage.getItem('usuario');

    if (!usuario) {
      alert('No se encontró el usuario de la sesión.');
      return;
    }

    // VALIDAR NOMBRES
  
    if (!this.perfil.nombres.trim()) {
      alert('Los nombres son obligatorios.');
      return;
    }

    // VALIDAR APELLIDOS

    if (!this.perfil.apellidos.trim()) {
      alert('Los apellidos son obligatorios.');
      return;
    }

    // VALIDAR DNI

    if (!this.perfil.dni.trim()) {
      alert('El DNI es obligatorio.');
      return;
    }

    if (!/^\d+$/.test(this.perfil.dni)) {
      alert('El DNI debe contener solo números.');
      return;
    }

    if (this.perfil.dni.length !== 8) {
      alert('El DNI debe contener exactamente 8 dígitos.');
      return;
    }

    // VALIDAR TELÉFONO

    if (!this.perfil.telefono.trim()) {
      alert('El teléfono es obligatorio.');
      return;
    }

    if (!/^\d+$/.test(this.perfil.telefono)) {
      alert('El teléfono debe contener solo números.');
      return;
    }

    if (this.perfil.telefono.length !== 9) {
      alert('El teléfono debe contener exactamente 9 dígitos.');
      return;
    }

    // ACTUALIZAR PERFIL

    this.guardando = true;

    const datos = {
      nombres: this.perfil.nombres.trim(),
      apellidos: this.perfil.apellidos.trim(),
      dni: this.perfil.dni,
      telefono: this.perfil.telefono
    };

    this.http
      .put(
        `http://localhost:8080/api/usuarios/${usuario}`,
        datos
      )
      .subscribe({

        next: (respuesta: any) => {

          this.guardando = false;

          alert(
            respuesta.mensaje ||
            'Perfil actualizado correctamente.'
          );
        },

        error: (error) => {

          console.error(
            'Error al actualizar el perfil:',
            error
          );

          this.guardando = false;

          alert(
            'No se pudo actualizar el perfil.'
          );
        }

      });
  }

  // CAMBIAR CONTRASEÑA

  cambiarPassword(): void {

    const usuario = sessionStorage.getItem('usuario');

    if (!usuario) {
      alert('No se encontró el usuario de la sesión.');
      return;
    }

    // VALIDAR CAMPOS

    if (!this.nuevaPassword || !this.confirmarPassword) {

      alert(
        'Complete ambos campos de contraseña.'
      );

      return;
    }

    // VALIDAR COINCIDENCIA

    if (
      this.nuevaPassword !==
      this.confirmarPassword
    ) {

      alert(
        'Las contraseñas no coinciden.'
      );

      return;
    }

    // VALIDAR LONGITUD

    if (this.nuevaPassword.length < 4) {

      alert(
        'La contraseña debe tener al menos 4 caracteres.'
      );

      return;
    }

    // ACTUALIZAR CONTRASEÑA

    this.cambiandoPassword = true;

    const datos = {
      nuevaPassword: this.nuevaPassword
    };

    this.http
      .put(
        `http://localhost:8080/api/usuarios/${usuario}/password`,
        datos
      )
      .subscribe({

        next: (respuesta: any) => {

          this.cambiandoPassword = false;

          this.nuevaPassword = '';
          this.confirmarPassword = '';

          alert(
            respuesta.mensaje ||
            'Contraseña actualizada correctamente.'
          );
        },

        error: (error) => {

          console.error(
            'Error al cambiar la contraseña:',
            error
          );

          this.cambiandoPassword = false;

          alert(
            'No se pudo cambiar la contraseña.'
          );
        }

      });
  }
}