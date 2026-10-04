import { Injectable, inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CotizacionRequest {
  idPreventa: number;
  formaPago: string;
}

@Injectable({
  providedIn: 'root'
})
export class CotizacionService {

  private apiUrl = 'http://localhost:8080/api/cotizaciones';

  private platformId = inject(PLATFORM_ID);

  constructor(private http: HttpClient) {}

  registrarCotizacion(
    cotizacion: CotizacionRequest
  ): Observable<any> {

    return this.http.post<any>(
      this.apiUrl,
      cotizacion
    );
  }

  listarCotizaciones(): Observable<any[]> {

    let usuario = '';
    let rol = '';

    if (isPlatformBrowser(this.platformId)) {
      usuario = sessionStorage.getItem('usuario') || '';
      rol = sessionStorage.getItem('rol') || '';
    }

    return this.http.get<any[]>(
      this.apiUrl,
      {
        params: {
          usuario,
          rol
        }
      }
    );
  }

  obtenerCotizacion(id: number): Observable<any> {

    return this.http.get<any>(
      `${this.apiUrl}/${id}`
    );
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