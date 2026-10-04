package com.facturacion.backend.entidad;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "preventa")
public class Preventa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPreventa;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String tipoVenta;

    @Column(nullable = false)
    private String condicionPago;

    private Integer plazoPago;

    @Column(nullable = false)
    private String transporte;

    private String agenciaTransporte;

    private String agenciaRuc;

    private String agenciaTelefono;

    @Column(nullable = false)
    private String razonSocial;

    @Column(nullable = false)
    private String ruc;

    private String telefono;

    private String direccion;

    private String urbanizacion;

    private String distrito;

    private String representanteCliente;

    private String vendedor;

    @Column(nullable = false)
    private Double descuentoPorcentaje = 0.0;

    @Column(nullable = false)
    private Double subtotalUsd = 0.0;

    @Column(nullable = false)
    private Double descuentoUsd = 0.0;

    @Column(nullable = false)
    private Double netoUsd = 0.0;

    @Column(nullable = false)
    private Double igvUsd = 0.0;

    @Column(nullable = false)
    private Double totalUsd = 0.0;

    /*
     * RELACIÓN CON CLIENTE
     * Una preventa pertenece a un cliente.* La columna id_cliente de la tabla preventa * referencia a cliente.id_cliente.
     */

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @JsonManagedReference
    @OneToMany(mappedBy = "preventa")
    private java.util.List<DetallePreventa> detalles;

    public Preventa() {
    }

    public Long getIdPreventa() {
        return idPreventa;
    }

    public void setIdPreventa(Long idPreventa) {
        this.idPreventa = idPreventa;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public java.util.List<DetallePreventa> getDetalles() {
        return detalles;
    }

    public void setDetalles(java.util.List<DetallePreventa> detalles) {
        this.detalles = detalles;
    }
}