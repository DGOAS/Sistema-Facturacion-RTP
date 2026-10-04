import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CotizacionService } from '../../services/cotizacion.service';

interface Cotizacion {
  idCotizacion: number;
  numero: string;
  fecha: string;
  formaPago: string;
  preventa: {
    idPreventa: number;
    numero: string;
    razonSocial: string;
    ruc: string;
  };
  subtotalUsd: number;
  igvUsd: number;
  totalUsd: number;
}

@Component({
  selector: 'app-consultar-cotizaciones',
  imports: [DecimalPipe, FormsModule, RouterLink],
  templateUrl: './consultar-cotizaciones.html',
  styleUrl: './consultar-cotizaciones.css'
})
export class ConsultarCotizaciones implements OnInit {

  cotizaciones: Cotizacion[] = [];
  cotizacionesFiltradas: Cotizacion[] = [];

  numeroBusqueda: string = '';
  clienteBusqueda: string = '';
  fechaBusqueda: string = '';

  // PAGINACIÓN
  paginaActual: number = 1;
  registrosPorPagina: number = 5;

  constructor(
    private cotizacionService: CotizacionService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarCotizaciones();
  }

  cargarCotizaciones(): void {

    this.cotizacionService.listarCotizaciones().subscribe({
      next: (respuesta: Cotizacion[]) => {

        this.cotizaciones = respuesta;
        this.cotizacionesFiltradas = respuesta;

        this.paginaActual = 1;

        this.cdr.detectChanges();

        console.log('Cotizaciones cargadas:', this.cotizaciones);

      },

      error: (error) => {

        console.error(
          'Error al cargar cotizaciones:',
          error
        );

      }
    });
  }

  filtrarCotizaciones(): void {

    this.cotizacionesFiltradas =
      this.cotizaciones.filter(cotizacion => {

        const coincideNumero =
          !this.numeroBusqueda ||
          cotizacion.numero
            .toLowerCase()
            .includes(
              this.numeroBusqueda.toLowerCase()
            );

        const coincideCliente =
          !this.clienteBusqueda ||
          cotizacion.preventa.razonSocial
            .toLowerCase()
            .includes(
              this.clienteBusqueda.toLowerCase()
            );

        const coincideFecha =
          !this.fechaBusqueda ||
          cotizacion.fecha === this.fechaBusqueda;

        return (
          coincideNumero &&
          coincideCliente &&
          coincideFecha
        );

      });

    // Volver a la primera página
    this.paginaActual = 1;
  }

  limpiarFiltros(): void {

    this.numeroBusqueda = '';
    this.clienteBusqueda = '';
    this.fechaBusqueda = '';

    this.cotizacionesFiltradas = this.cotizaciones;

    // Volver a la primera página
    this.paginaActual = 1;
  }

  // TOTAL DE PÁGINAS
  get totalPaginas(): number {

    return Math.ceil(
      this.cotizacionesFiltradas.length /
      this.registrosPorPagina
    );

  }

  // COTIZACIONES QUE SE MUESTRAN EN LA PÁGINA ACTUAL
  get cotizacionesPaginadas(): Cotizacion[] {

    const inicio =
      (this.paginaActual - 1) *
      this.registrosPorPagina;

    const fin =
      inicio +
      this.registrosPorPagina;

    return this.cotizacionesFiltradas.slice(
      inicio,
      fin
    );
  }

  // NÚMEROS DE PÁGINA
  get paginas(): number[] {

    return Array.from(
      { length: this.totalPaginas },
      (_, i) => i + 1
    );

  }

  // CAMBIAR DE PÁGINA
  cambiarPagina(pagina: number): void {

    if (
      pagina < 1 ||
      pagina > this.totalPaginas
    ) {
      return;
    }

    this.paginaActual = pagina;

  }

  paginaAnterior(): void {

    if (this.paginaActual > 1) {
      this.paginaActual--;
    }

  }

  paginaSiguiente(): void {

    if (
      this.paginaActual <
      this.totalPaginas
    ) {
      this.paginaActual++;
    }

  }

  // PRIMER REGISTRO MOSTRADO
  get indiceInicio(): number {

    if (this.cotizacionesFiltradas.length === 0) {
      return 0;
    }

    return (
      (this.paginaActual - 1) *
      this.registrosPorPagina
    ) + 1;

  }

  // ÚLTIMO REGISTRO MOSTRADO
  get indiceFin(): number {

    return Math.min(
      this.paginaActual *
      this.registrosPorPagina,
      this.cotizacionesFiltradas.length
    );

  }

}