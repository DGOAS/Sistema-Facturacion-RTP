package com.facturacion.backend.entidad;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_cotizacion")
public class DetalleCotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleCotizacion;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precioUnitarioUsd;

    @Column(nullable = false)
    private Double totalUsd;

    @com.fasterxml.jackson.annotation.JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_cotizacion", nullable = false)
    private Cotizacion cotizacion;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    public DetalleCotizacion() {
    }

    public Long getIdDetalleCotizacion() {
        return idDetalleCotizacion;
    }

    public void setIdDetalleCotizacion(Long idDetalleCotizacion) {
        this.idDetalleCotizacion = idDetalleCotizacion;
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

    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
