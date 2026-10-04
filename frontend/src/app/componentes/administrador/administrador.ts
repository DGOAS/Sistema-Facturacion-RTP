import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UsuarioService, Usuario } from '../../services/usuario.service';

@Component({
  selector: 'app-administrador',
  imports: [FormsModule],
  templateUrl: './administrador.html',
  styleUrl: './administrador.css'
})
export class Administrador implements OnInit {

  usuarios: Usuario[] = [];

  cargando: boolean = true;

  mostrarFormulario: boolean = false;

  modoEdicion: boolean = false;

  usuarioEditandoId: number | null = null;

  nuevoUsuario: Usuario = {
    usuario: '',
    password: '',
    nombres: '',
    apellidos: '',
    dni: '',
    telefono: '',
    rol: 'VENDEDOR',
    activo: true
  };

  constructor(
    private usuarioService: UsuarioService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  
  // CARGAR USUARIOS
  

  cargarUsuarios(): void {

    this.cargando = true;

    this.usuarioService.listarUsuarios().subscribe({

      next: (respuesta) => {

        console.log('Usuarios cargados:', respuesta);

        this.usuarios = respuesta;
        this.cargando = false;

        this.cdr.detectChanges();
      },

      error: (error) => {

        console.error('Error al cargar usuarios:', error);

        this.cargando = false;

        this.cdr.detectChanges();
      }

    });
  }

  
  // NUEVO USUARIO
  

  abrirNuevoUsuario(): void {

    this.modoEdicion = false;
    this.usuarioEditandoId = null;

    this.nuevoUsuario = {
      usuario: '',
      password: '',
      nombres: '',
      apellidos: '',
      dni: '',
      telefono: '',
      rol: 'VENDEDOR',
      activo: true
    };

    this.mostrarFormulario = true;

    this.cdr.detectChanges();
  }

  
  // CANCELAR
  

  cancelarFormulario(): void {

    this.mostrarFormulario = false;
    this.modoEdicion = false;
    this.usuarioEditandoId = null;

    this.nuevoUsuario = {
      usuario: '',
      password: '',
      nombres: '',
      apellidos: '',
      dni: '',
      telefono: '',
      rol: 'VENDEDOR',
      activo: true
    };

    this.cdr.detectChanges();
  }

  
  // EDITAR USUARIO
  

  editarUsuario(usuario: Usuario): void {

    this.modoEdicion = true;

    this.usuarioEditandoId = usuario.idUsuario ?? null;

    this.nuevoUsuario = {
      idUsuario: usuario.idUsuario,
      usuario: usuario.usuario,
      password: '',
      nombres: usuario.nombres,
      apellidos: usuario.apellidos,
      dni: usuario.dni,
      telefono: usuario.telefono,
      rol: usuario.rol,
      activo: usuario.activo
    };

    this.mostrarFormulario = true;

    this.cdr.detectChanges();
  }

  
  // VALIDAR FORMULARIO
  

  validarFormulario(): boolean {

    
    // USUARIO
    

    if (!this.nuevoUsuario.usuario.trim()) {

      alert('El usuario es obligatorio.');

      return false;
    }


    // CONTRASEÑA // 

    if (
      !this.modoEdicion &&
      !(this.nuevoUsuario.password ?? '').trim()
    ) {

      alert('La contraseña es obligatoria.');

      return false;
    }


    // NOMBRES // 

    if (!this.nuevoUsuario.nombres.trim()) {

      alert('Los nombres son obligatorios.');

      return false;
    }


    // APELLIDOS // 

    if (!this.nuevoUsuario.apellidos.trim()) {

      alert('Los apellidos son obligatorios.');

      return false;
    }


    // DNI // 

    if (!this.nuevoUsuario.dni.trim()) {

      alert('El DNI es obligatorio.');

      return false;
    }


    if (!/^\d+$/.test(this.nuevoUsuario.dni)) {

      alert('El DNI debe contener solo números.');

      return false;
    }


    if (this.nuevoUsuario.dni.length !== 8) {

      alert('El DNI debe contener exactamente 8 dígitos.');

      return false;
    }


    // TELÉFONO // 

    const telefono = this.nuevoUsuario.telefono?.trim() || '';

    if (telefono !== '') {

      if (!/^\d+$/.test(telefono)) {

        alert('El teléfono debe contener solo números.');

        return false;
      }


      if (telefono.length !== 9) {

        alert('El teléfono debe contener exactamente 9 dígitos.');

        return false;
      }
    }


    // ROL // 

    if (
      this.nuevoUsuario.rol !== 'ADMIN' &&
      this.nuevoUsuario.rol !== 'VENDEDOR'
    ) {

      alert('Debe seleccionar un rol válido.');

      return false;
    }


    return true;
  }

  
  // REGISTRAR USUARIO
  

  registrarUsuario(): void {

    if (!this.validarFormulario()) {
      return;
    }

    this.usuarioService
      .registrarUsuario(this.nuevoUsuario)
      .subscribe({

        next: () => {

          alert('Usuario registrado correctamente.');

          this.mostrarFormulario = false;
          this.modoEdicion = false;
          this.usuarioEditandoId = null;

          this.cargarUsuarios();
        },

        error: (error) => {

          console.error(
            'Error al registrar usuario:',
            error
          );

          alert('No se pudo registrar el usuario.');
        }

      });
  }

  
  // ACTUALIZAR USUARIO
  

  actualizarUsuario(): void {

    if (this.usuarioEditandoId === null) {

      alert('No se ha seleccionado un usuario para editar.');

      return;
    }


    if (!this.validarFormulario()) {
      return;
    }


    this.usuarioService
      .actualizarUsuario(
        this.usuarioEditandoId,
        this.nuevoUsuario
      )
      .subscribe({

        next: (respuesta) => {

          console.log(
            'Usuario actualizado:',
            respuesta
          );

          alert('Usuario actualizado correctamente.');

          this.mostrarFormulario = false;
          this.modoEdicion = false;
          this.usuarioEditandoId = null;

          this.cargarUsuarios();
        },

        error: (error) => {

          console.error(
            'Error al actualizar usuario:',
            error
          );

          alert('No se pudo actualizar el usuario.');
        }

      });
  }

  
  // INACTIVAR USUARIO
  

  inactivarUsuario(usuario: Usuario): void {

    if (usuario.idUsuario === undefined) {

      alert('El usuario no tiene un ID válido.');

      return;
    }


    const confirmar = window.confirm(
      `¿Deseas inactivar al usuario "${usuario.usuario}"?`
    );


    if (!confirmar) {
      return;
    }


    console.log(
      'Inactivando usuario:',
      usuario.idUsuario
    );


    this.usuarioService
      .inactivarUsuario(usuario.idUsuario)
      .subscribe({

        next: (respuesta) => {

          console.log(
            'Respuesta del servidor:',
            respuesta
          );

          alert('Usuario inactivado correctamente.');

          this.cargarUsuarios();
        },

        error: (error) => {

          console.error(
            'Error al inactivar usuario:',
            error
          );

          alert('No se pudo inactivar el usuario.');
        }

      });
  }


   
  // ACTIVAR USUARIO
  

  activarUsuario(usuario: Usuario): void {

    if (usuario.idUsuario === undefined) {

      alert('El usuario no tiene un ID válido.');

      return;
    }

    const confirmar = window.confirm(
      `¿Deseas activar al usuario "${usuario.usuario}"?`
    );

    if (!confirmar) {
      return;
    }

    console.log(
      'Activando usuario:',
      usuario.idUsuario
    );

    this.usuarioService
      .activarUsuario(usuario.idUsuario)
      .subscribe({

        next: (respuesta) => {

          console.log(
            'Respuesta del servidor:',
            respuesta
          );

          alert('Usuario activado correctamente.');

          this.cargarUsuarios();
        },

        error: (error) => {

          console.error(
            'Error al activar usuario:',
            error
          );

          alert('No se pudo activar el usuario.');
        }

      });
  }
}