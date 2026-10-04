package com.facturacion.backend.dto;

public class DetallePreventaRequest {

    private String codigo;
    private Integer cantidad;
    private Double precioUnitarioUsd;
    private Double totalUsd;

    public DetallePreventaRequest() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
