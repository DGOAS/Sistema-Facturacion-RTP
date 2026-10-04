package com.facturacion.backend.dto;

import com.facturacion.backend.entidad.Preventa;

public class PreventaConsultaResponse {

    private Long idPreventa;
    private String numero;
    private String fecha;
    private String razonSocial;
    private String ruc;
    private String tipoVenta;
    private String vendedor;
    private String estado;
    private Double totalUsd;

    public PreventaConsultaResponse(
            Preventa preventa,
            String estado
    ) {
        this.idPreventa = preventa.getIdPreventa();
        this.numero = preventa.getNumero();
        this.fecha = preventa.getFecha() != null
                ? preventa.getFecha().toString()
                : null;
        this.razonSocial = preventa.getRazonSocial();
        this.ruc = preventa.getRuc();
        this.tipoVenta = preventa.getTipoVenta();
        this.vendedor = preventa.getVendedor();
        this.estado = estado;
        this.totalUsd = preventa.getTotalUsd();
    }

    public Long getIdPreventa() {
        return idPreventa;
    }

    public String getNumero() {
        return numero;
    }

    public String getFecha() {
        return fecha;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public String getVendedor() {
        return vendedor;
    }

    public String getEstado() {
        return estado;
    }

    public Double getTotalUsd() {
        return totalUsd;
    }
}