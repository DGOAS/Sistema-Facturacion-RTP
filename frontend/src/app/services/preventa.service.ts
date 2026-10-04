import { Injectable, inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PreventaRequest {
  numero: string;
  fecha: string;

  idCliente: number;

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

  detalles: DetallePreventaRequest[];
}

export interface DetallePreventaRequest {
  codigo: string;
  cantidad: number;
  precioUnitarioUsd: number;
  totalUsd: number;
}

@Injectable({
  providedIn: 'root'
})
export class PreventaService {

  private apiUrl = 'http://localhost:8080/api/preventas';

  private platformId = inject(PLATFORM_ID);

  constructor(private http: HttpClient) {}

  registrarPreventa(preventa: PreventaRequest): Observable<any> {
    return this.http.post<any>(this.apiUrl, preventa);
  }

  listarPreventas(): Observable<any[]> {

    let usuario = '';
    let rol = '';

    if (isPlatformBrowser(this.platformId)) {
      usuario = sessionStorage.getItem('usuario') || '';
      rol = sessionStorage.getItem('rol') || '';
    }

    return this.http.get<any[]>(this.apiUrl, {
      params: {
        usuario,
        rol
      }
    });
  }

  obtenerPreventa(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  descargarPdf(id: number): Observable<Blob> {
    return this.http.get(
      `${this.apiUrl}/${id}/pdf`,
      {
        responseType: 'blob'
      }
    );
  }
}