import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { CotizacionService } from '../../services/cotizacion.service';

interface Producto {
  idProducto: number;
  codigo: string;
  codigoAnterior: string | null;
  descripcion: string;
  marca: string;
  nombre: string;
  precioPen: number;
  precioUsd: number;
}

interface DetalleCotizacionItem {
  idDetalleCotizacion: number;
  cantidad: number;
  precioUnitarioUsd: number;
  totalUsd: number;
  producto: Producto;
}

interface Preventa {
  idPreventa: number;
  numero: string;
  razonSocial: string;
  ruc: string;
  tipoVenta: string;
  transporte: string;
  representanteCliente: string;

  // Condición de pago de la preventa
  condicionPago: string;

  // Plazo de pago de la preventa
  plazoPago: number | null;
}

interface Cotizacion {
  idCotizacion: number;
  numero: string;
  fecha: string;

  formaPago: string;

  subtotalUsd: number;
  igvUsd: number;
  totalUsd: number;

  preventa: Preventa;

  detalles: DetalleCotizacionItem[];
}

@Component({
  selector: 'app-detalle-cotizacion',
  imports: [DecimalPipe],
  templateUrl: './detalle-cotizacion.html',
  styleUrl: './detalle-cotizacion.css'
})
export class DetalleCotizacion implements OnInit {

  idCotizacion: number = 0;

  cotizacion: Cotizacion | null = null;


  constructor(
    private route: ActivatedRoute,
    private cotizacionService: CotizacionService,
    private cdr: ChangeDetectorRef
  ) {}


  ngOnInit(): void {

    this.idCotizacion = Number(
      this.route.snapshot.paramMap.get('id')
    );

    console.log(
      'ID de cotización recibido:',
      this.idCotizacion
    );

    this.cargarCotizacion();
  }


  cargarCotizacion(): void {

    if (!this.idCotizacion) {

      console.error(
        'ID de cotización no válido'
      );

      return;
    }


    this.cotizacionService
      .obtenerCotizacion(this.idCotizacion)
      .subscribe({

        next: (respuesta: Cotizacion) => {

          
          if (respuesta.preventa) {

            respuesta.formaPago =
              respuesta.preventa.condicionPago;

          }


          this.cotizacion = respuesta;


          console.log(
            'Cotización cargada:',
            this.cotizacion
          );

          console.log(
            'Condición de pago de la preventa:',
            this.cotizacion.preventa?.condicionPago
          );

          console.log(
            'Plazo de pago de la preventa:',
            this.cotizacion.preventa?.plazoPago
          );


          this.cdr.detectChanges();

        },

        error: (error) => {

          console.error(
            'Error al cargar la cotización:',
            error
          );

        }

      });

  }


  descargarPdf(): void {

    if (!this.idCotizacion) {

      console.error(
        'ID de cotización no válido'
      );

      return;
    }


    this.cotizacionService
      .descargarPdf(this.idCotizacion)
      .subscribe({

        next: (archivo) => {

          const url =
            window.URL.createObjectURL(archivo);

          const enlace =
            document.createElement('a');

          enlace.href = url;

          enlace.download =
            `cotizacion-${
              this.cotizacion?.numero ??
              this.idCotizacion
            }.pdf`;

          enlace.click();

          window.URL.revokeObjectURL(url);

        },

        error: (error) => {

          console.error(
            'Error al descargar la cotización en PDF:',
            error
          );

          alert(
            'No se pudo descargar el PDF de la cotización.'
          );

        }

      });

  }

}