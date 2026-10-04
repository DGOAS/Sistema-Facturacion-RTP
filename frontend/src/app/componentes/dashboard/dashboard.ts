import { Component, OnInit, OnDestroy, ChangeDetectorRef, ViewChild, ElementRef, inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { PreventaService } from '../../services/preventa.service';
import { CotizacionService } from '../../services/cotizacion.service';
import { ClienteService } from '../../services/cliente.service';


interface PreventaDashboard {
  idPreventa: number;
  numero: string;
  fecha: string;
  razonSocial: string;
  totalUsd: number;
}


interface DetallePreventaDashboard {
  cantidad: number;

  producto?: {
    codigo: string;
    nombre: string;
  };
}


interface ProductoSolicitado {
  codigo: string;
  nombre: string;
  cantidad: number;
  porcentaje: number;
}


interface PreventaMes {
  mes: string;
  cantidad: number;
}


@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit, OnDestroy {

  // PLATAFORMA
 
  private platformId = inject(PLATFORM_ID);


  
  // REFERENCIA AL GRÁFICO
  
  @ViewChild('graficoPreventas')
  graficoPreventas!: ElementRef<HTMLCanvasElement>;

  private chartPreventas: any;


  // ESTADÍSTICAS
  
  totalPreventas: number = 0;
  valorTotalPreventas: number = 0;
  totalCotizaciones: number = 0;
  totalClientes: number = 0;


  // ÚLTIMAS PREVENTAS
   ultimasPreventas: PreventaDashboard[] = [];

  // PREVENTAS POR MES
  preventasPorMes: PreventaMes[] = [];

  // PRODUCTOS MÁS SOLICITADOS
  productosMasSolicitados: ProductoSolicitado[] = [];

  // ESTADO DE CARGA
  cargando: boolean = true;

  // CONSTRUCTOR
  

  constructor(
    private preventaService: PreventaService,
    private cotizacionService: CotizacionService,
    private clienteService: ClienteService,
    private cdr: ChangeDetectorRef
  ) {}


  // INICIO
  

  ngOnInit(): void {

    this.cargarDashboard();

  }

  // DESTRUIR COMPONENTE
  

  ngOnDestroy(): void {

    if (this.chartPreventas) {

      this.chartPreventas.destroy();

      this.chartPreventas = null;

    }

  }

  // CARGAR DASHBOARD

  cargarDashboard(): void {

    this.cargando = true;

    // PREVENTAS
    

    this.preventaService.listarPreventas().subscribe({

      next: (preventas) => {

        const lista: PreventaDashboard[] =
          preventas as PreventaDashboard[];

      
        // TOTAL DE PREVENTAS
        
        this.totalPreventas =
          lista.length;


        // VALOR TOTAL DE PREVENTAS
        
        this.valorTotalPreventas =
          lista.reduce(
            (total, preventa) =>
              total + Number(preventa.totalUsd || 0),
            0
          );


        // ÚLTIMAS PREVENTAS
        

        this.ultimasPreventas =
          [...lista]
            .sort((a, b) => {

              const fechaA =
                new Date(a.fecha).getTime();

              const fechaB =
                new Date(b.fecha).getTime();

              return fechaB - fechaA;

            })
            .slice(0, 5);


        // PREVENTAS POR MES

        this.calcularPreventasPorMes(lista);


        // PRODUCTOS MÁS SOLICITADOS

        this.cargarDetallesProductos(lista);

        // FINALIZAR CARGA PRINCIPAL


        this.cargando = false;
        this.cdr.detectChanges();


        // CREAR GRÁFICO SOLO EN NAVEGADOR

        if (isPlatformBrowser(this.platformId)) {

          setTimeout(() => {

            this.crearGraficoPreventas();

          });

        }

      },


      error: (error) => {

        console.error(
          'Error al cargar preventas del dashboard:',
          error
        );

        this.cargando = false;

        this.cdr.detectChanges();

      }

    });


    // COTIZACIONES

    this.cotizacionService.listarCotizaciones().subscribe({

      next: (cotizaciones) => {

        this.totalCotizaciones =
          cotizaciones.length;

        this.cdr.detectChanges();

      },


      error: (error) => {

        console.error(
          'Error al cargar cotizaciones del dashboard:',
          error
        );

      }

    });


    // CLIENTES

    this.clienteService.listarClientes().subscribe({

      next: (clientes) => {

        this.totalClientes =
          clientes.length;

        this.cdr.detectChanges();

      },


      error: (error) => {

        console.error(
          'Error al cargar clientes del dashboard:',
          error
        );

      }

    });

  }


  // CARGAR DETALLES DE LAS PREVENTAS

  cargarDetallesProductos(
    preventas: PreventaDashboard[]
  ): void {

    if (preventas.length === 0) {

      this.productosMasSolicitados = [];

      this.cdr.detectChanges();

      return;

    }


    const solicitudes =
      preventas.map(preventa =>

        this.preventaService
          .obtenerPreventa(preventa.idPreventa)
          .pipe(

            catchError(error => {

              console.error(
                `Error al obtener detalle de preventa ${preventa.idPreventa}:`,
                error
              );

              return of(null);

            })

          )

      );


    forkJoin(solicitudes).subscribe({

      next: (respuestas) => {

        const detalles:
          DetallePreventaDashboard[] = [];


        // RECORRER RESPUESTAS
        
        respuestas.forEach((preventa: any) => {

          if (
            preventa &&
            Array.isArray(preventa.detalles)
          ) {

            detalles.push(
              ...preventa.detalles
            );

          }

        });


        // CALCULAR PRODUCTOS

        this.calcularProductosMasSolicitados(
          detalles
        );


        this.cdr.detectChanges();

      },


      error: (error) => {

        console.error(
          'Error al cargar los detalles de las preventas:',
          error
        );

        this.productosMasSolicitados = [];

        this.cdr.detectChanges();

      }

    });

  }

  // PREVENTAS POR MES

  calcularPreventasPorMes(
    preventas: PreventaDashboard[]
  ): void {

    const meses = [

      'Ene',
      'Feb',
      'Mar',
      'Abr',
      'May',
      'Jun',
      'Jul',
      'Ago',
      'Sep',
      'Oct',
      'Nov',
      'Dic'

    ];


    const cantidades: number[] =
      new Array(12).fill(0);


    preventas.forEach(preventa => {

      if (!preventa.fecha) {
        return;
      }


      const fecha =
        new Date(preventa.fecha);


      if (isNaN(fecha.getTime())) {
        return;
      }


      const mes =
        fecha.getMonth();


      cantidades[mes]++;

    });


    this.preventasPorMes =
      meses.map((nombre, indice) => ({

        mes: nombre,

        cantidad:
          cantidades[indice]

      }));

  }

  // PRODUCTOS MÁS SOLICITADOS


  calcularProductosMasSolicitados(
    detalles: DetallePreventaDashboard[]
  ): void {

    const productos = new Map<
      string,
      {
        codigo: string;
        nombre: string;
        cantidad: number;
      }
    >();


    // AGRUPAR POR PRODUCTO
  

    detalles.forEach(detalle => {

      if (!detalle.producto) {
        return;
      }


      const codigo =
        detalle.producto.codigo;


      const cantidad =
        Number(detalle.cantidad || 0);


      const existente =
        productos.get(codigo);


      if (existente) {

        existente.cantidad +=
          cantidad;

      } else {

        productos.set(codigo, {

          codigo:
            detalle.producto.codigo,

          nombre:
            detalle.producto.nombre,

          cantidad:
            cantidad

        });

      }

    });


    // ORDENAR Y TOMAR LOS 5 PRIMEROS
    
    const lista =
      Array.from(productos.values())
        .sort(
          (a, b) =>
            b.cantidad - a.cantidad
        )
        .slice(0, 5);


    // CANTIDAD MÁXIMA

    const cantidadMaxima =
      Math.max(

        ...lista.map(
          producto =>
            producto.cantidad
        ),

        1

      );

    // PREPARAR DATOS PARA LA VISTA

    this.productosMasSolicitados =
      lista.map(producto => ({

        codigo:
          producto.codigo,

        nombre:
          producto.nombre,

        cantidad:
          producto.cantidad,

        porcentaje:
          (
            producto.cantidad /
            cantidadMaxima
          ) * 100

      }));

  }


  // CREAR GRÁFICO CHART.JS
 
  async crearGraficoPreventas(): Promise<void> {

    // SOLO NAVEGADOR
    
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    // COMPROBAR CANVAS

    if (!this.graficoPreventas) {
      return;
    }

    // CARGAR CHART.JS DINÁMICAMENTE

    const { Chart } =
      await import('chart.js/auto');

    // DESTRUIR GRÁFICO ANTERIOR

    if (this.chartPreventas) {

      this.chartPreventas.destroy();

    }


    // OBTENER CANVAS

    const canvas =
      this.graficoPreventas.nativeElement;


    const contexto =
      canvas.getContext('2d');


    if (!contexto) {
      return;
    }


    // CREAR GRÁFICO

    this.chartPreventas = new Chart(

      contexto,

      {

        type: 'bar',


        data: {

          labels:
            this.preventasPorMes.map(
              item =>
                item.mes
            ),


          datasets: [

            {

              label:
                'Preventas',


              data:
                this.preventasPorMes.map(
                  item =>
                    item.cantidad
                ),


              backgroundColor:
                '#2379ed',


              borderColor:
                '#2379ed',


              borderWidth:
                1,


              borderRadius:
                6,


              barPercentage:
                0.55,


              categoryPercentage:
                0.75

            }

          ]

        },


        options: {

          responsive:
            true,


          maintainAspectRatio:
            false,


          plugins: {

            legend: {

              display:
                false

            },


            tooltip: {

              callbacks: {

                label: (context: any) => {

                  return ` ${
                    context.parsed.y
                  } preventa${
                    context.parsed.y === 1
                      ? ''
                      : 's'
                  }`;

                }

              }

            }

          },


          scales: {

            x: {

              grid: {

                display:
                  false

              },


              ticks: {

                color:
                  '#6b7280'

              }

            },


            y: {

              beginAtZero:
                true,


              ticks: {

                stepSize:
                  1,

                precision:
                  0,

                color:
                  '#6b7280'

              },


              grid: {

                color:
                  '#e5e7eb'

              }

            }

          }

        }

      }

    );

  }

  // FORMATO DE FECHA

  formatearFecha(
    fecha: string
  ): string {

    if (!fecha) {
      return '-';
    }


    const partes =
      fecha.split('-');


    if (partes.length !== 3) {
      return fecha;
    }


    return `${partes[2]}/${partes[1]}/${partes[0]}`;

  }


  // FORMATO MONETARIO

  formatearMonto(
    monto: number
  ): string {

    return Number(
      monto || 0
    ).toFixed(2);

  }

}