package com.facturacion.backend.dto;

public class CotizacionRequest {

    private Long idPreventa;
    private String formaPago;

    public CotizacionRequest() {
    }

    public Long getIdPreventa() {
        return idPreventa;
    }

    public void setIdPreventa(Long idPreventa) {
        this.idPreventa = idPreventa;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }
}