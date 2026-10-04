import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ProductoService, Producto } from '../../services/producto.service';
import { PreventaService } from '../../services/preventa.service';
import { ClienteService, Cliente } from '../../services/cliente.service';

interface Repuesto {
  codigo: string;
  nombre: string;
  descripcion: string;
  precioPEN: number;
  precioUSD: number;
}

interface DetalleRepuesto extends Repuesto {
  cantidad: number;
  precioUnitario: number;
  total: number;
}

@Component({
  selector: 'app-preventa',
  imports: [FormsModule],
  templateUrl: './preventa.html',
  styleUrl: './preventa.css'
})
export class Preventa implements OnInit {

  constructor(
    private productoService: ProductoService,
    private preventaService: PreventaService,
    private clienteService: ClienteService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    this.productoService.listarProductos().subscribe({
      next: (productos: Producto[]) => {

        this.repuestosDisponibles = productos.map(producto => ({
          codigo: producto.codigo,
          nombre: producto.nombre,
          descripcion: producto.descripcion,
          precioPEN: producto.precioPen,
          precioUSD: producto.precioUsd
        }));

        console.log('Productos cargados:', this.repuestosDisponibles);
      },

      error: (error) => {
        console.error('Error al cargar productos:', error);
      }
    });

    this.cargarClientes();
  }

  // CLIENTES

  clientes: Cliente[] = [];

  idCliente: number | null = null;

  cargarClientes(): void {

    this.clienteService.listarClientes().subscribe({
      next: (clientes) => {

        this.clientes = clientes;

        console.log('Clientes cargados:', this.clientes);
      },

      error: (error) => {
        console.error('Error al cargar clientes:', error);
      }
    });
  }


  seleccionarCliente(): void {

    if (!this.idCliente) {
      this.limpiarDatosCliente();
      return;
    }

    const clienteSeleccionado = this.clientes.find(
      cliente => cliente.idCliente === Number(this.idCliente)
    );

    if (!clienteSeleccionado) {
      this.limpiarDatosCliente();
      return;
    }

    this.cliente = clienteSeleccionado.razonSocial;
    this.ruc = clienteSeleccionado.ruc;
    this.telefono = clienteSeleccionado.telefono || '';
    this.direccion = clienteSeleccionado.direccion || '';
    this.urbanizacion = clienteSeleccionado.urbanizacion || '';
    this.distrito = clienteSeleccionado.distrito || '';
  }


  limpiarDatosCliente(): void {

    this.cliente = '';
    this.ruc = '';
    this.telefono = '';
    this.direccion = '';
    this.urbanizacion = '';
    this.distrito = '';
  }


  // CATÁLOGO DE REPUESTOS

  repuestosDisponibles: Repuesto[] = [];

  // REPUESTOS AGREGADOS

  repuestosAgregados: DetalleRepuesto[] = [];
  repuestoSeleccionado: Repuesto | null = null;
  cantidad: number = 1;


  // DATOS DE LA OPERACIÓN
  
  tipoVenta: string = '';
  transporte: string = '';

  // CONDICIONES DE PAGO

  condicionPago: string = '';
  plazoPago: number | null = null;

  // DATOS DE LA AGENCIA

  agenciaTransporte: string = '';
  agenciaRuc: string = '';
  agenciaTelefono: string = '';


  // DATOS DEL CLIENTE


  cliente: string = '';
  ruc: string = '';
  telefono: string = '';
  direccion: string = '';
  urbanizacion: string = '';
  distrito: string = '';

  // RECEPCIÓN

  representanteCliente: string = '';

  // CONSULTA OPCIONAL EN SOLES

  tipoCambio: number = 0;

  // DESCUENTO

  descuento: number = 0;

  // CÁLCULOS

  get subtotal(): number {

    return this.repuestosAgregados.reduce(
      (suma, repuesto) => suma + repuesto.total,
      0
    );
  }


  get puedeAplicarDescuento(): boolean {

    return this.subtotal >= 5000;
  }


  validarDescuento(): void {

    if (this.descuento < 0) {
      this.descuento = 0;
    }

    if (this.descuento > 10) {

      this.descuento = 10;

      alert(
        'El descuento máximo permitido es 10%.'
      );
    }

    if (!this.puedeAplicarDescuento) {
      this.descuento = 0;
    }
  }


  validarCondicionPago(): boolean {

    if (
      this.condicionPago === 'FACTURA' ||
      this.condicionPago === 'LETRA'
    ) {

      if (
        this.plazoPago === null ||
        this.plazoPago === undefined ||
        this.plazoPago <= 0
      ) {

        alert(
          'Debe ingresar el plazo en días para la condición de pago seleccionada.'
        );

        return false;
      }
    }

    return true;
  }


  get descuentoMonto(): number {

    return this.subtotal * (this.descuento / 100);
  }


  get baseConDescuento(): number {

    return this.subtotal - this.descuentoMonto;
  }


  get igv(): number {

    return this.baseConDescuento * 0.18;
  }


  get total(): number {

    return this.baseConDescuento + this.igv;
  }


  // AGREGAR REPUESTO

  agregarRepuesto(): void {

    if (!this.repuestoSeleccionado) {

      alert('Seleccione un repuesto.');

      return;
    }

    if (this.cantidad <= 0) {

      alert('La cantidad debe ser mayor a 0.');

      return;
    }

    const precio =
      this.repuestoSeleccionado.precioUSD;

    const repuestoExistente =
      this.repuestosAgregados.find(
        repuesto =>
          repuesto.codigo ===
          this.repuestoSeleccionado?.codigo
      );

    if (repuestoExistente) {

      repuestoExistente.cantidad +=
        this.cantidad;

      repuestoExistente.total =
        repuestoExistente.precioUnitario *
        repuestoExistente.cantidad;

    } else {

      const total =
        precio * this.cantidad;

      const detalle: DetalleRepuesto = {

        ...this.repuestoSeleccionado,

        cantidad: this.cantidad,

        precioUnitario: precio,

        total: total
      };

      this.repuestosAgregados.push(
        detalle
      );
    }

    this.repuestoSeleccionado = null;

    this.cantidad = 1;
  }


  eliminarRepuesto(codigo: string): void {

    this.repuestosAgregados =
      this.repuestosAgregados.filter(
        repuesto =>
          repuesto.codigo !== codigo
      );

    if (!this.puedeAplicarDescuento) {
      this.descuento = 0;
    }
  }


  // LIMPIAR FORMULARIO

  limpiarFormulario(): void {

    console.log('LIMPIANDO FORMULARIO...');

    // Cliente
    this.idCliente = null;

    this.cliente = '';
    this.ruc = '';
    this.telefono = '';
    this.direccion = '';
    this.urbanizacion = '';
    this.distrito = '';

    // Operación
    this.tipoVenta = '';

    this.transporte = '';

    // Condición de pago
    this.condicionPago = '';

    this.plazoPago = null;

    // Agencia
    this.agenciaTransporte = '';
    this.agenciaRuc = '';
    this.agenciaTelefono = '';

    // Representante
    this.representanteCliente = '';

    // Tipo de cambio
    this.tipoCambio = 0;

    // Descuento
    this.descuento = 0;

    // Repuestos
    this.repuestosAgregados = [];

    this.repuestoSeleccionado = null;

    this.cantidad = 1;

    // Forzar actualización visual de Angular
    this.cdr.detectChanges();
  }


  // REGISTRAR PREVENTA

  registrarPreventa(): void {

    if (this.repuestosAgregados.length === 0) {

      alert(
        'Debe agregar al menos un repuesto.'
      );

      return;
    }

    if (!this.idCliente) {

      alert(
        'Seleccione un cliente.'
      );

      return;
    }

    if (!this.tipoVenta) {

      alert(
        'Seleccione el tipo de venta.'
      );

      return;
    }

    if (!this.condicionPago) {

      alert(
        'Seleccione la condición de pago.'
      );

      return;
    }

    if (!this.validarCondicionPago()) {

      return;
    }

    if (!this.transporte) {

      alert(
        'Seleccione el transporte.'
      );

      return;
    }

    if (!this.representanteCliente.trim()) {

      alert(
        'Debe ingresar el representante del cliente autorizado.'
      );

      return;
    }

    const preventa = {

      numero: '',

      fecha: '',

      idCliente: this.idCliente,

      vendedor:
        sessionStorage.getItem('usuario') || '',

      tipoVenta:
        this.tipoVenta,

      condicionPago:
        this.condicionPago,

      plazoPago:
        this.plazoPago,

      transporte:
        this.transporte,

      agenciaTransporte:
        this.agenciaTransporte,

      agenciaRuc:
        this.agenciaRuc,

      agenciaTelefono:
        this.agenciaTelefono,

      razonSocial:
        this.cliente,

      ruc:
        this.ruc,

      telefono:
        this.telefono,

      direccion:
        this.direccion,

      urbanizacion:
        this.urbanizacion,

      distrito:
        this.distrito,

      representanteCliente:
        this.representanteCliente,

      descuentoPorcentaje:
        this.descuento,

      subtotalUsd:
        this.subtotal,

      descuentoUsd:
        this.descuentoMonto,

      netoUsd:
        this.baseConDescuento,

      igvUsd:
        this.igv,

      totalUsd:
        this.total,

      detalles:
        this.repuestosAgregados.map(
          repuesto => ({

            codigo:
              repuesto.codigo,

            cantidad:
              repuesto.cantidad,

            precioUnitarioUsd:
              repuesto.precioUnitario,

            totalUsd:
              repuesto.total

          })
        )
    };


    this.preventaService
      .registrarPreventa(preventa)
      .subscribe({

        next: (respuesta) => {

          console.log(
            'Preventa registrada:',
            respuesta
          );

          alert(
            'Preventa registrada correctamente: ' +
            respuesta.numero
          );

          // Limpiar el formulario
          // después de guardar correctamente.
          this.limpiarFormulario();
        },

        error: (error) => {

          console.error(
            'Error al registrar la preventa:',
            error
          );

          alert(
            'No se pudo registrar la preventa.'
          );
        }

      });
  }
}