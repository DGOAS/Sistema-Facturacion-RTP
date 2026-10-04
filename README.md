# Sistema de Facturación - RTP Repuestos

Sistema web desarrollado para la gestión de preventas, cotizaciones, clientes, productos y usuarios de RTP Repuestos.

## Tecnologías utilizadas

### Frontend
- Angular 22.1.7
- TypeScript
- HTML5
- CSS3

### Backend
- Java
- Spring Boot
- Maven
- Spring Data JPA
- Hibernate

### Base de datos
- Microsoft SQL Server
- SQL Server Management Studio (SSMS)

## Estructura del proyecto

```text
Sistema-Facturacion-RTP/
├── backend/                 # API REST desarrollada con Spring Boot
├── frontend/                # Aplicación web desarrollada con Angular
├── bd/
│   └── FACTURACION_DB.sql   # Script de base de datos con estructura y datos
├── .gitignore
└── README.md
```

## Requisitos

Para ejecutar el proyecto se necesita:

- Node.js
- npm
- Angular CLI
- Java JDK
- Maven
- Microsoft SQL Server
- SQL Server Management Studio (SSMS)

## Base de datos

El proyecto incluye el script:

```text
bd/FACTURACION_DB.sql
```

Este script contiene la estructura y los datos de la base de datos `FACTURACION_DB`, incluyendo productos, clientes, usuarios, preventas, cotizaciones y sus respectivos detalles.

### Instalación de la base de datos

1. Abrir SQL Server Management Studio.
2. Abrir el archivo `bd/FACTURACION_DB.sql`.
3. Ejecutar el script.
4. Verificar que se haya creado la base de datos `FACTURACION_DB`.

## Configuración del backend

La configuración de conexión a SQL Server se encuentra en:

```text
backend/src/main/resources/application.properties
```

Antes de ejecutar el backend, se deben configurar las credenciales de SQL Server correspondientes al entorno local.

En el repositorio se utiliza un valor de reemplazo para la contraseña:

```properties
spring.datasource.password=CAMBIAR_PASSWORD_LOCAL
```

Este valor debe sustituirse por la contraseña correspondiente de la instalación local de SQL Server.

## Ejecutar el backend

Desde la carpeta del proyecto:

```bash
cd backend
```

En Windows:

```bash
.\mvnw.cmd spring-boot:run
```

El backend se ejecuta normalmente en:

```text
http://localhost:8080
```

## Ejecutar el frontend

Desde la carpeta del proyecto:

```bash
cd frontend
```

Instalar las dependencias:

```bash
npm install
```

Ejecutar Angular:

```bash
ng serve
```

La aplicación estará disponible en:

```text
http://localhost:4200
```

## Usuarios de prueba

### Administrador

- Usuario: `admin`
- Contraseña: `0987`
- Rol: Administrador

### Vendedor

- Usuario: `diego`
- Contraseña: `4321`
- Rol: Vendedor

Estas cuentas se pueden utilizar para realizar las pruebas del sistema según el rol correspondiente.

## Funcionalidades principales

- Inicio de sesión y autenticación de usuarios.
- Gestión de usuarios y roles.
- Gestión de clientes.
- Consulta de productos y repuestos.
- Registro de preventas.
- Gestión de condiciones de pago.
- Registro del representante autorizado del cliente.
- Selección del tipo de transporte.
- Cálculo de subtotal, IGV y total.
- Aplicación de descuentos según las reglas del negocio.
- Generación de cotizaciones.
- Consulta de preventas y cotizaciones.
- Visualización del detalle de documentos.
- Generación de documentos PDF.
- Gestión de usuarios administradores y vendedores.
- Activación e inactivación de usuarios.
- Actualización de los datos del perfil.
- Integración con base de datos SQL Server.

## Flujo principal del sistema

```text
Cliente
   ↓
Nueva Preventa
   ↓
Selección de productos
   ↓
Condiciones de pago
   ↓
Registro de Preventa
   ↓
Generación de Cotización
   ↓
Consulta / Detalle
   ↓
Generación de PDF
```

## Proyecto académico

**Curso:** Soluciones Web

**Proyecto:** Sistema de Facturación - RTP Repuestos

El proyecto fue desarrollado utilizando Angular para el frontend, Spring Boot para el backend y Microsoft SQL Server para la gestión de la base de datos.
