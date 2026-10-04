package com.facturacion.backend.dto;

import java.util.List;

public class PreventaRequest {

    private String numero;
    private String fecha;
    private String tipoVenta;
    private String condicionPago;
    private Integer plazoPago;
    private String transporte;

    private String agenciaTransporte;
    private String agenciaRuc;
    private String agenciaTelefono;

    private Long idCliente;

    private String razonSocial;
    private String ruc;
    private String telefono;
    private String direccion;
    private String urbanizacion;
    private String distrito;
    private String representanteCliente;
    private String vendedor;

    private Double descuentoPorcentaje;
    private Double subtotalUsd;
    private Double descuentoUsd;
    private Double netoUsd;
    private Double igvUsd;
    private Double totalUsd;

    private List<DetallePreventaRequest> detalles;

    public PreventaRequest() {
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public String getCondicionPago() {
        return condicionPago;
    }

    public void setCondicionPago(String condicionPago) {
        this.condicionPago = condicionPago;
    }

    public Integer getPlazoPago() {
        return plazoPago;
    }

    public void setPlazoPago(Integer plazoPago) {
        this.plazoPago = plazoPago;
    }

    public String getTransporte() {
        return transporte;
    }

    public void setTransporte(String transporte) {
        this.transporte = transporte;
    }

    public String getAgenciaTransporte() {
        return agenciaTransporte;
    }

    public void setAgenciaTransporte(String agenciaTransporte) {
        this.agenciaTransporte = agenciaTransporte;
    }

    public String getAgenciaRuc() {
        return agenciaRuc;
    }

    public void setAgenciaRuc(String agenciaRuc) {
        this.agenciaRuc = agenciaRuc;
    }

    public String getAgenciaTelefono() {
        return agenciaTelefono;
    }

    public void setAgenciaTelefono(String agenciaTelefono) {
        this.agenciaTelefono = agenciaTelefono;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUrbanizacion() {
        return urbanizacion;
    }

    public void setUrbanizacion(String urbanizacion) {
        this.urbanizacion = urbanizacion;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getRepresentanteCliente() {
        return representanteCliente;
    }

    public void setRepresentanteCliente(String representanteCliente) {
        this.representanteCliente = representanteCliente;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public Double getDescuentoPorcentaje() {
        return descuentoPorcentaje;
    }

    public void setDescuentoPorcentaje(Double descuentoPorcentaje) {
        this.descuentoPorcentaje = descuentoPorcentaje;
    }

    public Double getSubtotalUsd() {
        return subtotalUsd;
    }

    public void setSubtotalUsd(Double subtotalUsd) {
        this.subtotalUsd = subtotalUsd;
    }

    public Double getDescuentoUsd() {
        return descuentoUsd;
    }

    public void setDescuentoUsd(Double descuentoUsd) {
        this.descuentoUsd = descuentoUsd;
    }

    public Double getNetoUsd() {
        return netoUsd;
    }

    public void setNetoUsd(Double netoUsd) {
        this.netoUsd = netoUsd;
    }

    public Double getIgvUsd() {
        return igvUsd;
    }

    public void setIgvUsd(Double igvUsd) {
        this.igvUsd = igvUsd;
    }

    public Double getTotalUsd() {
        return totalUsd;
    }

    public void setTotalUsd(Double totalUsd) {
        this.totalUsd = totalUsd;
    }

    public List<DetallePreventaRequest> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePreventaRequest> detalles) {
        this.detalles = detalles;
    }
}