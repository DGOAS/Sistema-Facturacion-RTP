import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { PreventaService } from '../../services/preventa.service';

interface Preventa {
  idPreventa: number;
  numero: string;
  fecha: string;
  razonSocial: string;
  ruc: string;
  tipoVenta: string;
  vendedor: string | null;
  estado: string;
  totalUsd: number;
}

@Component({
  selector: 'app-consultar-preventas',
  imports: [FormsModule, RouterLink, DecimalPipe],
  templateUrl: './consultar-preventas.html',
  styleUrl: './consultar-preventas.css'
})
export class ConsultarPreventas implements OnInit {

  preventas: Preventa[] = [];
  preventasFiltradas: Preventa[] = [];
  preventasPagina: Preventa[] = [];

  numeroBusqueda: string = '';
  clienteBusqueda: string = '';
  fechaBusqueda: string = '';
  vendedorBusqueda: string = '';
  estadoBusqueda: string = '';

  // PAGINACIÓN
  paginaActual: number = 1;
  preventasPorPagina: number = 5;

  constructor(
    private preventaService: PreventaService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarPreventas();
  }

  cargarPreventas(): void {

    this.preventaService.listarPreventas().subscribe({

      next: (respuesta: Preventa[]) => {

        this.preventas = respuesta;
        this.preventasFiltradas = respuesta;

        this.paginaActual = 1;

        this.actualizarPagina();

        this.cdr.detectChanges();

        console.log('Preventas cargadas:', this.preventas.length);
      },

      error: (error) => {

        console.error(
          'Error al cargar las preventas:',
          error
        );

      }

    });
  }

  buscarPreventas(): void {

    this.preventasFiltradas = this.preventas.filter(preventa => {

      const coincideNumero =
        !this.numeroBusqueda ||
        preventa.numero
          .toLowerCase()
          .includes(this.numeroBusqueda.toLowerCase());

      const coincideCliente =
        !this.clienteBusqueda ||
        preventa.razonSocial
          .toLowerCase()
          .includes(this.clienteBusqueda.toLowerCase());

      const coincideFecha =
        !this.fechaBusqueda ||
        preventa.fecha === this.fechaBusqueda;

      const coincideVendedor =
        !this.vendedorBusqueda ||
        (preventa.vendedor || '')
          .toLowerCase()
          .includes(this.vendedorBusqueda.toLowerCase());

      const coincideEstado =
        !this.estadoBusqueda ||
        preventa.estado === this.estadoBusqueda;

      return (
        coincideNumero &&
        coincideCliente &&
        coincideFecha &&
        coincideVendedor &&
        coincideEstado
      );
    });

    // Volver a la primera página después de buscar
    this.paginaActual = 1;

    this.actualizarPagina();
  }

  limpiarFiltros(): void {

    this.numeroBusqueda = '';
    this.clienteBusqueda = '';
    this.fechaBusqueda = '';
    this.vendedorBusqueda = '';
    this.estadoBusqueda = '';

    this.preventasFiltradas = this.preventas;

    this.paginaActual = 1;

    this.actualizarPagina();
  }

  // =========================
  // PAGINACIÓN
  // =========================

  actualizarPagina(): void {

    const inicio =
      (this.paginaActual - 1) * this.preventasPorPagina;

    const fin =
      inicio + this.preventasPorPagina;

    this.preventasPagina =
      this.preventasFiltradas.slice(inicio, fin);
  }

  obtenerTotalPaginas(): number {

    return Math.ceil(
      this.preventasFiltradas.length /
      this.preventasPorPagina
    );
  }

  irAPagina(pagina: number): void {

    if (
      pagina < 1 ||
      pagina > this.obtenerTotalPaginas()
    ) {
      return;
    }

    this.paginaActual = pagina;

    this.actualizarPagina();
  }

  paginaAnterior(): void {

    this.irAPagina(
      this.paginaActual - 1
    );
  }

  paginaSiguiente(): void {

    this.irAPagina(
      this.paginaActual + 1
    );
  }

  obtenerPaginasVisibles(): number[] {

    const totalPaginas =
      this.obtenerTotalPaginas();

    const paginas: number[] = [];

    for (
      let i = 1;
      i <= totalPaginas;
      i++
    ) {
      paginas.push(i);
    }

    return paginas;
  }

  obtenerInicioResultados(): number {

    if (this.preventasFiltradas.length === 0) {
      return 0;
    }

    return (
      (this.paginaActual - 1) *
      this.preventasPorPagina
    ) + 1;
  }

  obtenerFinResultados(): number {

    return Math.min(
      this.paginaActual *
      this.preventasPorPagina,
      this.preventasFiltradas.length
    );
  }
}