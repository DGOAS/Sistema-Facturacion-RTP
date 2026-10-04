import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  imports: [FormsModule],
  selector: 'app-login',
  styleUrl: './login.css',
  templateUrl: './login.html',
})
export class Login {

  usuario: string = '';
  password: string = '';

  constructor(private http: HttpClient, private router: Router) {}

  ingresar(): void {

    const datos = {
      usuario: this.usuario,
      password: this.password
    };

    this.http.post('http://localhost:8080/api/login', datos).subscribe({

      next: (respuesta: any) => {
        console.log('Respuesta del servidor:', respuesta);

        sessionStorage.setItem('usuario', respuesta.usuario);
        sessionStorage.setItem('rol', respuesta.rol);

        this.router.navigate(['/inicio']);
      },

      error: (error) => {
        console.error('Error de login:', error);
        alert('Usuario o contraseña incorrectos');
      }

    });
  }
}