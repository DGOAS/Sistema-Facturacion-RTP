import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { PreventaService } from '../../services/preventa.service';
import { CotizacionService } from '../../services/cotizacion.service';
import { FormsModule } from '@angular/forms';

interface Documento {
  id: number;
  numero: string;
  tipo: 'Preventa' | 'Cotización';
  fecha: string;
  referencia: string;
  cliente: string;
  ruc: string;
  totalUsd: number;
}

@Component({
  selector: 'app-documentos',
  imports: [
    RouterLink,
    DecimalPipe,
    FormsModule
  ],
  templateUrl: './documentos.html',
  styleUrl: './documentos.css'
})
export class Documentos implements OnInit {

  
  // DOCUMENTOS
 
  documentos: Documento[] = [];
  documentosFiltrados: Documento[] = [];

  // Documentos que se muestran en la página actual
  documentosPaginados: Documento[] = [];

  // FILTROS

  textoBusqueda: string = '';
  tipoSeleccionado: string = 'Todos';

  // ESTADO
  
  cargando: boolean = true;

  // PAGINACIÓN
  
  paginaActual: number = 1;
  documentosPorPagina: number = 10;


  constructor(
    private preventaService: PreventaService,
    private cotizacionService: CotizacionService,
    private cdr: ChangeDetectorRef
  ) {}

  // INICIO
  
  ngOnInit(): void {
    this.cargarDocumentos();
  }

  // CARGA DOCUMENTOS
  

  cargarDocumentos(): void {
    this.cargando = true;
    let preventasCargadas = false;
    let cotizacionesCargadas = false;


    // Carga preventas
  
    this.preventaService.listarPreventas().subscribe({

      next: (preventas) => {

        const documentosPreventas: Documento[] =
          preventas.map((preventa: any) => ({

            id: preventa.idPreventa,

            numero: preventa.numero,

            tipo: 'Preventa',

            fecha: preventa.fecha,

            referencia: preventa.numero,

            cliente: preventa.razonSocial || '-',

            ruc: preventa.ruc || '-',

            totalUsd: Number(preventa.totalUsd || 0)

          }));


        this.documentos = [

          ...this.documentos.filter(
            documento => documento.tipo !== 'Preventa'
          ),

          ...documentosPreventas

        ];


        preventasCargadas = true;

        this.finalizarCarga(
          preventasCargadas,
          cotizacionesCargadas
        );
      },


      error: (error) => {

        console.error(
          'Error al cargar preventas:',
          error
        );

        preventasCargadas = true;

        this.finalizarCarga(
          preventasCargadas,
          cotizacionesCargadas
        );
      }

    });


    // Carga cotizaciones

    this.cotizacionService.listarCotizaciones().subscribe({

      next: (cotizaciones) => {

        const documentosCotizaciones: Documento[] =
          cotizaciones.map((cotizacion: any) => ({

            id: cotizacion.idCotizacion,

            numero: cotizacion.numero,

            tipo: 'Cotización',

            fecha: cotizacion.fecha,

            referencia:
              cotizacion.preventa?.numero || '-',

            cliente:
              cotizacion.preventa?.razonSocial || '-',

            ruc:
              cotizacion.preventa?.ruc || '-',

            totalUsd:
              Number(cotizacion.totalUsd || 0)

          }));


        this.documentos = [

          ...this.documentos.filter(
            documento => documento.tipo !== 'Cotización'
          ),

          ...documentosCotizaciones

        ];


        cotizacionesCargadas = true;

        this.finalizarCarga(
          preventasCargadas,
          cotizacionesCargadas
        );
      },


      error: (error) => {

        console.error(
          'Error al cargar cotizaciones:',
          error
        );

        cotizacionesCargadas = true;

        this.finalizarCarga(
          preventasCargadas,
          cotizacionesCargadas
        );
      }

    });

  }


  
  // FINALIZAR CARGA
  

  finalizarCarga(
    preventasCargadas: boolean,
    cotizacionesCargadas: boolean
  ): void {

    if (
      !preventasCargadas ||
      !cotizacionesCargadas
    ) {
      return;
    }


    // Ordena por fecha más reciente
    this.documentos.sort((a, b) => {

      const fechaA =
        new Date(a.fecha).getTime();

      const fechaB =
        new Date(b.fecha).getTime();

      return fechaB - fechaA;

    });


    // Aplica filtros y prepara primera página
    this.aplicarFiltros();
    this.cargando = false;
    this.cdr.detectChanges();

  }


  // FILTROS
  

  aplicarFiltros(): void {

    const texto =
      this.textoBusqueda
        .trim()
        .toLowerCase();


    this.documentosFiltrados =
      this.documentos.filter(documento => {

        const coincideTipo =
          this.tipoSeleccionado === 'Todos' ||
          documento.tipo === this.tipoSeleccionado;


        const coincideTexto =
          !texto ||

          documento.numero
            .toLowerCase()
            .includes(texto) ||

          documento.cliente
            .toLowerCase()
            .includes(texto) ||

          documento.ruc
            .toLowerCase()
            .includes(texto) ||

          documento.referencia
            .toLowerCase()
            .includes(texto);


        return coincideTipo && coincideTexto;

      });


    // Cuando cambia la búsqueda o filtro, vuelve siempre a la primera página.
    this.paginaActual = 1;
    this.actualizarPaginacion();

  }

  // CAMBIAR TIPO
  

  cambiarTipo(): void {

    this.aplicarFiltros();
  }


  // LIMPIAR FILTROS
  

  limpiarFiltros(): void {

    this.textoBusqueda = '';
    this.tipoSeleccionado = 'Todos';
    this.aplicarFiltros();

  }

  
  // PAGINACIÓN
  
  actualizarPaginacion(): void {

    const inicio =
      (this.paginaActual - 1) *
      this.documentosPorPagina;

    const fin =
      inicio +
      this.documentosPorPagina;

    this.documentosPaginados =
      this.documentosFiltrados.slice(
        inicio,
        fin
      );

  }

  // TOTAL DE PÁGINAS
  

  obtenerTotalPaginas(): number {

    return Math.ceil(
      this.documentosFiltrados.length /
      this.documentosPorPagina
    );

  }


  
  // LISTA DE PÁGINAS
  

  obtenerPaginas(): number[] {

    return Array.from(
      {
        length: this.obtenerTotalPaginas()
      },
      (_, i) => i + 1
    );

  }


 
  // CAMBIAR DE PÁGINA
  

  cambiarPagina(pagina: number): void {

    const totalPaginas =
      this.obtenerTotalPaginas();


    if (
      pagina < 1 ||
      pagina > totalPaginas
    ) {
      return;
    }


    this.paginaActual = pagina;

    this.actualizarPaginacion();

  }


  
  // PÁGINA ANTERIOR
  

  paginaAnterior(): void {

    if (this.paginaActual > 1) {

      this.paginaActual--;

      this.actualizarPaginacion();

    }

  }

  // PÁGINA SIGUIENTE
  
  paginaSiguiente(): void {

    const totalPaginas =
      this.obtenerTotalPaginas();


    if (
      this.paginaActual < totalPaginas
    ) {

      this.paginaActual++;

      this.actualizarPaginacion();

    }

  }


  // DESCARGAR DOCUMENTO
  

  descargarDocumento(
    documento: Documento
  ): void {

    // PREVENTA
  

    if (documento.tipo === 'Preventa') {

      this.preventaService
        .descargarPdf(documento.id)
        .subscribe({

          next: (archivo) => {

            this.descargarArchivo(
              archivo,
              `preventa-${documento.numero}.pdf`
            );

          },


          error: (error) => {

            console.error(
              'Error al descargar PDF de preventa:',
              error
            );

            alert(
              'No se pudo descargar el PDF de la preventa.'
            );

          }

        });

    }

    // COTIZACIÓN

    else {

      this.cotizacionService
        .descargarPdf(documento.id)
        .subscribe({

          next: (archivo) => {

            this.descargarArchivo(
              archivo,
              `cotizacion-${documento.numero}.pdf`
            );

          },


          error: (error) => {

            console.error(
              'Error al descargar PDF de cotización:',
              error
            );

            alert(
              'No se pudo descargar el PDF de la cotización.'
            );

          }

        });

    }

  }

  
  // DESCARGAR ARCHIVO
  

  descargarArchivo(
    archivo: Blob,
    nombre: string
  ): void {

    const url =
      window.URL.createObjectURL(archivo);


    const enlace =
      document.createElement('a');


    enlace.href = url;

    enlace.download = nombre;


    enlace.click();


    window.URL.revokeObjectURL(url);

  }


  
  // RUTA DE DETALLE
  

  obtenerRutaDetalle(
    documento: Documento
  ): string {

    if (
      documento.tipo === 'Preventa'
    ) {

      return `/detalle-preventa/${documento.id}`;

    }


    return `/detalle-cotizacion/${documento.id}`;

  }

    obtenerUltimoDocumento(): number {

    return Math.min(
      this.paginaActual * this.documentosPorPagina,
      this.documentosFiltrados.length
    );

  }

}