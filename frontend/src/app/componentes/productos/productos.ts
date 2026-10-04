import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductoService, Producto } from '../../services/producto.service';

@Component({
  selector: 'app-productos',
  imports: [FormsModule, DecimalPipe],
  templateUrl: './productos.html',
  styleUrl: './productos.css'
})
export class Productos implements OnInit {

  Math = Math;
  productos: Producto[] = [];
  productosFiltrados: Producto[] = [];
  productosPagina: Producto[] = [];

  textoBusqueda: string = '';

  cargando: boolean = true;

  // Paginación
  paginaActual: number = 1;
  productosPorPagina: number = 20;

  constructor(
    private productoService: ProductoService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
  }

  cargarProductos(): void {

    this.cargando = true;

    this.productoService.listarProductos().subscribe({

      next: (respuesta) => {

        console.log('Productos cargados:', respuesta.length);

        this.productos = respuesta;
        this.productosFiltrados = respuesta;

        this.paginaActual = 1;

        this.actualizarPagina();

        this.cargando = false;

        this.cdr.detectChanges();
      },

      error: (error) => {

        console.error('Error al cargar productos:', error);

        this.cargando = false;

        this.cdr.detectChanges();
      }

    });
  }

  buscarProductos(): void {

    const texto = this.textoBusqueda
      .trim()
      .toLowerCase();

    if (!texto) {

      this.productosFiltrados = this.productos;

    } else {

      this.productosFiltrados = this.productos.filter(producto => {

        return (
          producto.codigo.toLowerCase().includes(texto) ||
          (producto.codigoAnterior || '').toLowerCase().includes(texto) ||
          producto.nombre.toLowerCase().includes(texto) ||
          producto.descripcion.toLowerCase().includes(texto) ||
          producto.marca.toLowerCase().includes(texto)
        );

      });
    }

    // Cuando se realiza una nueva búsqueda,vuelve a la primera página.
    this.paginaActual = 1;

    this.actualizarPagina();
  }

  limpiarBusqueda(): void {

    this.textoBusqueda = '';

    this.productosFiltrados = this.productos;

    this.paginaActual = 1;

    this.actualizarPagina();
  }

  actualizarPagina(): void {

    const inicio =
      (this.paginaActual - 1) * this.productosPorPagina;

    const fin =
      inicio + this.productosPorPagina;

    this.productosPagina =
      this.productosFiltrados.slice(inicio, fin);
  }

  obtenerTotalPaginas(): number {

    return Math.ceil(
      this.productosFiltrados.length /
      this.productosPorPagina
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

    this.irAPagina(this.paginaActual - 1);
  }

  paginaSiguiente(): void {

    this.irAPagina(this.paginaActual + 1);
  }

  cambiarProductosPorPagina(): void {

    this.paginaActual = 1;

    this.actualizarPagina();
  }

  obtenerPaginasVisibles(): number[] {

    const totalPaginas = this.obtenerTotalPaginas();

    const paginas: number[] = [];

    for (let i = 1; i <= totalPaginas; i++) {

      paginas.push(i);

    }

    return paginas;
  }
}