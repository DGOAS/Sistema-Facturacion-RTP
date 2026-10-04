import {
  Component,
  OnInit,
  ChangeDetectorRef
} from '@angular/core';

import { DecimalPipe } from '@angular/common';

import {
  ActivatedRoute,
  Router
} from '@angular/router';

import { FormsModule } from '@angular/forms';

import { PreventaService } from '../../services/preventa.service';

import { CotizacionService } from '../../services/cotizacion.service';


interface Producto {

  codigo: string;

  nombre: string;

}


interface DetallePreventa {

  idDetalle: number;

  cantidad: number;

  precioUnitarioUsd: number;

  totalUsd: number;

  producto: Producto;

}


interface Preventa {

  idPreventa: number;

  numero: string;

  fecha: string;

  tipoVenta: string;

  condicionPago: string;

  plazoPago: number | null;

  transporte: string;

  agenciaTransporte: string;

  agenciaRuc: string;

  agenciaTelefono: string;

  razonSocial: string;

  ruc: string;

  telefono: string;

  direccion: string;

  urbanizacion: string;

  distrito: string;

  representanteCliente: string;

  descuentoPorcentaje: number;

  subtotalUsd: number;

  descuentoUsd: number;

  netoUsd: number;

  igvUsd: number;

  totalUsd: number;

  detalles: DetallePreventa[];

}


@Component({

  selector: 'app-cotizacion',

  imports: [
    DecimalPipe,
    FormsModule
  ],

  templateUrl: './cotizacion.html',

  styleUrl: './cotizacion.css'

})


export class Cotizacion implements OnInit {


  // =========================================================
  // DATOS
  // =========================================================

  idPreventa: number = 0;

  preventa: Preventa | null = null;


  // =========================================================
  // FORMA DE PAGO
  // =========================================================

  formaPago: string = '';


  // =========================================================
  // ESTADO
  // =========================================================

  guardando: boolean = false;


  // =========================================================
  // CONSTRUCTOR
  // =========================================================

  constructor(

    private route: ActivatedRoute,

    private router: Router,

    private preventaService: PreventaService,

    private cotizacionService: CotizacionService,

    private cdr: ChangeDetectorRef

  ) {}


  // =========================================================
  // INICIO
  // =========================================================

  ngOnInit(): void {

    this.idPreventa =
      Number(
        this.route.snapshot.paramMap.get('id')
      );


    console.log(
      'ID de preventa recibido:',
      this.idPreventa
    );


    this.cargarPreventa();

  }


  // =========================================================
  // CARGAR PREVENTA
  // =========================================================

  cargarPreventa(): void {

    if (!this.idPreventa) {

      alert(
        'No se encontró la preventa.'
      );

      return;

    }


    this.preventaService
      .obtenerPreventa(this.idPreventa)
      .subscribe({

        next: (respuesta) => {

          console.log(
            'Preventa cargada para cotización:',
            respuesta
          );


          this.preventa = respuesta;


          /*
           * La cotización hereda automáticamente
           * la condición de pago de la preventa.
           */
          this.formaPago =
            respuesta.condicionPago;


          this.cdr.detectChanges();

        },


        error: (error) => {

          console.error(
            'Error al cargar preventa:',
            error
          );


          alert(
            'No se pudo cargar la información de la preventa.'
          );

        }

      });

  }


  // =========================================================
  // CANCELAR
  // =========================================================

  cancelar(): void {

    this.router.navigate([
      '/consultar-preventas'
    ]);

  }


  // =========================================================
  // GUARDAR COTIZACIÓN
  // =========================================================

  guardarCotizacion(): void {

    if (!this.idPreventa) {

      alert(
        'No se encontró la preventa.'
      );

      return;

    }


    if (
      !this.formaPago ||
      this.formaPago.trim() === ''
    ) {

      alert(
        'Seleccione una forma de pago.'
      );

      return;

    }


    this.guardando = true;


    const request = {

      idPreventa:
        this.idPreventa,

      formaPago:
        this.formaPago

    };


    this.cotizacionService
      .registrarCotizacion(request)
      .subscribe({

        next: (respuesta) => {

          console.log(
            'Cotización registrada:',
            respuesta
          );


          alert(
            'Cotización registrada correctamente.'
          );


          this.guardando = false;


          this.router.navigate([
            '/consultar-cotizaciones'
          ]);

        },


        error: (error) => {

          console.error(
            'Error al registrar cotización:',
            error
          );


          this.guardando = false;


          alert(
            'No se pudo registrar la cotización.'
          );

        }

      });

  }

}