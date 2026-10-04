package com.facturacion.backend.entidad;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "detalle_preventa")
public class DetallePreventa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_preventa", nullable = false)
    private Preventa preventa;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precioUnitarioUsd;

    @Column(nullable = false)
    private Double totalUsd;

    public DetallePreventa() {
    }

    public Long getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Preventa getPreventa() {
        return preventa;
    }

    public void setPreventa(Preventa preventa) {
        this.preventa = preventa;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitarioUsd() {
        return precioUnitarioUsd;
    }

    public void setPrecioUnitarioUsd(Double precioUnitarioUsd) {
        this.precioUnitarioUsd = precioUnitarioUsd;
    }

    public Double getTotalUsd() {
        return totalUsd;
    }

    public void setTotalUsd(Double totalUsd) {
        this.totalUsd = totalUsd;
    }
}
