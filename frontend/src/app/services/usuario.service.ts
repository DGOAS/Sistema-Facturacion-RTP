import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Usuario {
  idUsuario?: number;
  usuario: string;
  password?: string;
  nombres: string;
  apellidos: string;
  dni: string;
  telefono: string;
  rol: string;
  activo?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private apiUrl = 'http://localhost:8080/api/usuarios';

  constructor(private http: HttpClient) {}

  listarUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.apiUrl}/admin`);
  }

  registrarUsuario(usuario: Usuario): Observable<any> {
    return this.http.post(`${this.apiUrl}/admin`, usuario);
  }

  actualizarUsuario(id: number, usuario: Usuario): Observable<any> {
    return this.http.put(`${this.apiUrl}/admin/${id}`, usuario);
  }

  inactivarUsuario(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/admin/${id}`);
  }

  activarUsuario(id: number): Observable<any> {
    return this.http.put(
      `${this.apiUrl}/admin/${id}/activar`,
      {}
    );
  }
}