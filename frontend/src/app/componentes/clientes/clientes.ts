import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Cliente, ClienteService } from '../../services/cliente.service';

@Component({
  selector: 'app-clientes',
  imports: [FormsModule],
  templateUrl: './clientes.html',
  styleUrl: './clientes.css'
})
export class Clientes implements OnInit {

  clientes: Cliente[] = [];

  mostrarFormulario: boolean = false;
  editando: boolean = false;

  clienteSeleccionado: Cliente = this.nuevoCliente();

  constructor(
    private clienteService: ClienteService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarClientes();
  }


  // CARGAR CLIENTES // 

  cargarClientes(): void {

    this.clienteService.listarClientes().subscribe({

      next: (respuesta) => {

        this.clientes = respuesta;

        this.cdr.detectChanges();
      },

      error: (error) => {

        console.error(
          'Error al cargar clientes:',
          error
        );

        alert('No se pudieron cargar los clientes.');
      }

    });
  }


  // CLIENTE NUEVO // 

  nuevoCliente(): Cliente {

    return {

      razonSocial: '',
      ruc: '',
      telefono: '',
      direccion: '',
      urbanizacion: '',
      distrito: ''

    };
  }


  // ABRIR FORMULARIO // 

  abrirFormulario(): void {

    this.editando = false;

    this.clienteSeleccionado =
      this.nuevoCliente();

    this.mostrarFormulario = true;
  }


  // CANCELAR / CERRAR FORMULARIO // 

  cancelarFormulario(): void {

    this.mostrarFormulario = false;

    this.editando = false;

    this.clienteSeleccionado =
      this.nuevoCliente();
  }


  // VALIDAR RUC MIENTRAS SE ESCRIBE // 

  limpiarRuc(valor: string): void {

    const valorOriginal = valor || '';

    /* Si contiene letras o símbolos,mostrar alerta.*/

    if (/\D/.test(valorOriginal)) {

      alert(
        'El RUC solo debe contener números.'
      );
    }

    /* Eliminar cualquier carácter que no sea número y limitar a 11 dígitos*/

    this.clienteSeleccionado.ruc =
      valorOriginal
        .replace(/\D/g, '')
        .slice(0, 11);
  }


  // VALIDAR TELÉFONO MIENTRAS SE ESCRIBE // 

  limpiarTelefono(valor: string): void {

    const valorOriginal = valor || '';

    /* Si contiene letras o símbolos,mostrar alerta.*/

    if (/\D/.test(valorOriginal)) {

      alert(
        'El teléfono solo debe contener números.'
      );
    }

    /*Eliminar cualquier carácter que no sea número y limitar a 9 dígitos*/

    this.clienteSeleccionado.telefono =
      valorOriginal
        .replace(/\D/g, '')
        .slice(0, 9);
  }


  // GUARDAR CLIENTE // 

  guardarCliente(): void {


    // VALIDAR RAZÓN SOCIAL // 

    if (
      !this.clienteSeleccionado.razonSocial ||
      !this.clienteSeleccionado.razonSocial.trim()
    ) {

      alert(
        'La razón social es obligatoria.'
      );

      return;
    }


    // VALIDAR RUC 

    const ruc =
      (this.clienteSeleccionado.ruc || '').trim();


    // RUC vacío

    if (!ruc) {

      alert(
        'El RUC es obligatorio.'
      );

      return;
    }


    // RUC con letras o caracteres especiales

    if (!/^\d+$/.test(ruc)) {

      alert(
        'El RUC solo debe contener números.'
      );

      return;
    }


    // RUC debe tener exactamente 11 dígitos

    if (ruc.length !== 11) {

      alert(
        'El RUC debe contener exactamente 11 dígitos.'
      );

      return;
    }


    
    // VALIDAR TELÉFONO
    

    const telefono =
      (this.clienteSeleccionado.telefono || '').trim();



    if (telefono) {


      // Teléfono con letras o símbolos

      if (!/^\d+$/.test(telefono)) {

        alert(
          'El teléfono solo debe contener números.'
        );

        return;
      }


      // Teléfono debe tener 9 dígitos

      if (telefono.length !== 9) {

        alert(
          'El teléfono debe contener exactamente 9 dígitos.'
        );

        return;
      }
    }


    // ACTUALIZAR CLIENTE
    

    if (
      this.editando &&
      this.clienteSeleccionado.idCliente
    ) {

      this.clienteService
        .actualizarCliente(
          this.clienteSeleccionado.idCliente,
          this.clienteSeleccionado
        )
        .subscribe({

          next: () => {

            alert(
              'Cliente actualizado correctamente.'
            );

            this.cancelarFormulario();

            this.cargarClientes();
          },

          error: (error) => {

            console.error(
              'Error al actualizar cliente:',
              error
            );

            alert(
              'No se pudo actualizar el cliente.'
            );
          }

        });

    } else {


      
      // REGISTRAR NUEVO CLIENTE
      

      this.clienteService
        .guardarCliente(
          this.clienteSeleccionado
        )
        .subscribe({

          next: () => {

            alert(
              'Cliente registrado correctamente.'
            );

            this.cancelarFormulario();

            this.cargarClientes();
          },

          error: (error) => {

            console.error(
              'Error al registrar cliente:',
              error
            );

            alert(
              'No se pudo registrar el cliente.'
            );
          }

        });
    }
  }


  
  // EDITAR CLIENTE
  

  editarCliente(cliente: Cliente): void {

    this.editando = true;

    this.clienteSeleccionado = {
      ...cliente
    };

    this.mostrarFormulario = true;
  }


  
  // DESACTIVAR CLIENTE
  

  eliminarCliente(cliente: Cliente): void {

    if (!cliente.idCliente) {

      return;
    }


    const confirmar = confirm(
      `¿Deseas desactivar al cliente "${cliente.razonSocial}"?`
    );


    if (!confirmar) {

      return;
    }


    this.clienteService
      .desactivarCliente(
        cliente.idCliente
      )
      .subscribe({

        next: () => {

          alert(
            'Cliente desactivado correctamente.'
          );

          this.cargarClientes();
        },

        error: (error) => {

          console.error(
            'Error al desactivar cliente:',
            error
          );

          alert(
            'No se pudo desactivar el cliente.'
          );
        }

      });
  }

}