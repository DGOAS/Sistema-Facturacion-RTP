import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Producto {
  idProducto: number;
  codigo: string;
  codigoAnterior: string | null;
  descripcion: string;
  marca: string;
  nombre: string;
  precioPen: number;
  precioUsd: number;
}

@Injectable({
  providedIn: 'root'
})
export class ProductoService {

  private apiUrl = 'http://localhost:8080/api/productos';

  constructor(private http: HttpClient) {}

  listarProductos(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.apiUrl);
  }
}