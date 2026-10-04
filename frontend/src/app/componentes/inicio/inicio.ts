import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
@Component({
  selector: 'app-inicio',
  imports: [],
  templateUrl: './inicio.html',
  styleUrl: './inicio.css'
})
export class Inicio implements OnInit, OnDestroy {

  constructor(private cdr: ChangeDetectorRef) {}
  slideActual: number = 0;

  private intervalo!: ReturnType<typeof setInterval>;

  slides = [
    {
      imagen: '/fachada-rtp.png',
      titulo: 'RTP REPUESTOS',
      subtitulo: 'Soluciones para el sector automotriz',
      descripcion: 'Somos una empresa dedicada a la comercialización de repuestos automotrices, ofreciendo variedad de productos y soluciones para nuestros clientes.'
    },
    {
      imagen: '/almacen-rtp.png',
      titulo: 'AMPLIA VARIEDAD',
      subtitulo: 'Repuestos para el sector automotriz',
      descripcion: 'Contamos con una variedad de productos destinados a diferentes necesidades del sector automotriz.'
    },
    {
      imagen: '/respuestos-rtp.png',
      titulo: 'CALIDAD Y GARANTÍA',
      subtitulo: 'Soluciones para nuestros clientes',
      descripcion: 'Trabajamos para ofrecer productos y una gestión comercial organizada y eficiente.'
    }
  ];

  ngOnInit(): void {
  this.intervalo = setInterval(() => {
    this.siguienteSlide();
    this.cdr.detectChanges();
  }, 5000);
}

  ngOnDestroy(): void {
    clearInterval(this.intervalo);
  }

  siguienteSlide(): void {
    console.log('CAMBIO DE SLIDE:', this.slideActual);

    this.slideActual =
      (this.slideActual + 1) % this.slides.length;
  }

  anteriorSlide(): void {
    this.slideActual =
      (this.slideActual - 1 + this.slides.length) % this.slides.length;
  }
}