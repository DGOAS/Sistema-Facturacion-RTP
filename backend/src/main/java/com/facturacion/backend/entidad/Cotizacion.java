package com.facturacion.backend.entidad;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cotizacion")
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCotizacion;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "forma_pago")
    private String formaPago;

    @ManyToOne
    @JoinColumn(name = "id_preventa", nullable = false)
    private Preventa preventa;

    @Column(nullable = false)
    private Double subtotalUsd = 0.0;

    @Column(nullable = false)
    private Double igvUsd = 0.0;

    @Column(nullable = false)
    private Double totalUsd = 0.0;

    @OneToMany(mappedBy = "cotizacion")
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<DetalleCotizacion> detalles;

    public Cotizacion() {
    }

    public Long getIdCotizacion() {
        return idCotizacion;
    }

    public void setIdCotizacion(Long idCotizacion) {
        this.idCotizacion = idCotizacion;
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

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public Preventa getPreventa() {
        return preventa;
    }

    public void setPreventa(Preventa preventa) {
        this.preventa = preventa;
    }

    public Double getSubtotalUsd() {
        return subtotalUsd;
    }

    public void setSubtotalUsd(Double subtotalUsd) {
        this.subtotalUsd = subtotalUsd;
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

    public List<DetalleCotizacion> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCotizacion> detalles) {
        this.detalles = detalles;
    }
}