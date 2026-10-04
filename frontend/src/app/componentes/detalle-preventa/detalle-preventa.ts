import { ActivatedRoute } from '@angular/router';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { PreventaService } from '../../services/preventa.service';

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

interface Detalle {
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
  detalles: Detalle[];
}

@Component({
  selector: 'app-detalle-preventa',
  imports: [DecimalPipe, RouterLink],
  templateUrl: './detalle-preventa.html',
  styleUrl: './detalle-preventa.css'
})
export class DetallePreventa implements OnInit {

  numeroPreventa = '';
  preventa: Preventa | null = null;

  constructor(
  private route: ActivatedRoute,
  private preventaService: PreventaService,
  private cdr: ChangeDetectorRef
) {}

  ngOnInit(): void {
    this.numeroPreventa =
      this.route.snapshot.paramMap.get('id') ?? '';

    this.cargarPreventa();
  }

  cargarPreventa(): void {
    const id = Number(this.numeroPreventa);

    if (!id) {
      console.error('ID de preventa no válido');
      return;
    }

    this.preventaService.obtenerPreventa(id).subscribe({
      next: (respuesta) => {
        this.preventa = respuesta;

        this.cdr.detectChanges();

        console.log('Preventa cargada:', this.preventa);
      },
      error: (error) => {
        console.error('Error al cargar la preventa:', error);
      }
    });
  }

descargarPdf(): void {

    if (!this.numeroPreventa) {
      console.error('ID de preventa no válido');
      return;
    }

    const id = Number(this.numeroPreventa);

    this.preventaService.descargarPdf(id).subscribe({
      next: (archivo) => {

        const url = window.URL.createObjectURL(archivo);

        const enlace = document.createElement('a');
        enlace.href = url;
        enlace.download = `preventa-${this.preventa?.numero ?? id}.pdf`;

        enlace.click();

        window.URL.revokeObjectURL(url);
      },

      error: (error) => {
        console.error('Error al descargar el PDF:', error);
        alert('No se pudo descargar el PDF de la preventa.');
      }
    });
  }

}