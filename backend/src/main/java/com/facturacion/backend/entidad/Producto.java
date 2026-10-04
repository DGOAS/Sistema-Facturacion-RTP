package com.facturacion.backend.entidad;

import jakarta.persistence.*;

@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    @Column(nullable = false, unique = true)
    private String codigo;

    private String codigoAnterior;

    private String marca;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(nullable = false)
    private Double precioUsd;

    @Column(nullable = false)
    private Double precioPen;

    public Producto() {
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoAnterior() {
        return codigoAnterior;
    }

    public void setCodigoAnterior(String codigoAnterior) {
        this.codigoAnterior = codigoAnterior;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecioUsd() {
        return precioUsd;
    }

    public void setPrecioUsd(Double precioUsd) {
        this.precioUsd = precioUsd;
    }

    public Double getPrecioPen() {
        return precioPen;
    }

    public void setPrecioPen(Double precioPen) {
        this.precioPen = precioPen;
    }
}
