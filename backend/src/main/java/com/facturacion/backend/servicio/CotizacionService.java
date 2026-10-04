package com.facturacion.backend.servicio;

import com.facturacion.backend.dto.CotizacionRequest;
import com.facturacion.backend.entidad.Cotizacion;
import com.facturacion.backend.entidad.DetalleCotizacion;
import com.facturacion.backend.entidad.DetallePreventa;
import com.facturacion.backend.entidad.Producto;
import com.facturacion.backend.entidad.Preventa;
import com.facturacion.backend.repositorio.CotizacionRepository;
import com.facturacion.backend.repositorio.DetalleCotizacionRepository;
import com.facturacion.backend.repositorio.PreventaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CotizacionService {

    private final CotizacionRepository cotizacionRepository;
    private final DetalleCotizacionRepository detalleCotizacionRepository;
    private final PreventaRepository preventaRepository;

    public CotizacionService(
            CotizacionRepository cotizacionRepository,
            DetalleCotizacionRepository detalleCotizacionRepository,
            PreventaRepository preventaRepository
    ) {
        this.cotizacionRepository = cotizacionRepository;
        this.detalleCotizacionRepository = detalleCotizacionRepository;
        this.preventaRepository = preventaRepository;
    }

    @Transactional
    public Cotizacion registrarCotizacion(CotizacionRequest request) {

        // Buscar la preventa de origen
        if (request.getIdPreventa() == null) {
            throw new RuntimeException(
                    "Debe seleccionar una preventa para generar la cotización."
            );
        }

        Preventa preventa = preventaRepository.findById(request.getIdPreventa())
                .orElseThrow(() ->
                        new RuntimeException(
                                "No se encontró la preventa con ID: "
                                        + request.getIdPreventa()
                        )
                );
        
        if (cotizacionRepository.existsByPreventa_IdPreventa(request.getIdPreventa())) {
                throw new RuntimeException(
                        "La preventa seleccionada ya tiene una cotización registrada."
                );
        }

        
        // Validar forma de pago
        if (request.getFormaPago() == null
                || request.getFormaPago().trim().isEmpty()) {

            throw new RuntimeException(
                    "La forma de pago es obligatoria."
            );
        }

        
        if (preventa.getTipoVenta() == null
                || preventa.getTipoVenta().trim().isEmpty()) {

            throw new RuntimeException(
                    "No se puede generar la cotización: "
                            + "la preventa no tiene tipo de venta."
            );
        }

        if (preventa.getCondicionPago() == null
                || preventa.getCondicionPago().trim().isEmpty()) {

            throw new RuntimeException(
                    "No se puede generar la cotización: "
                            + "la preventa no tiene condición de pago."
            );
        }

        
        String condicionPago = preventa.getCondicionPago();

        if (("FACTURA".equalsIgnoreCase(condicionPago)
                || "LETRA".equalsIgnoreCase(condicionPago))
                && (preventa.getPlazoPago() == null
                || preventa.getPlazoPago() <= 0)) {

            throw new RuntimeException(
                    "No se puede generar la cotización: "
                            + "la preventa no tiene un plazo de pago válido."
            );
        }

        if (preventa.getTransporte() == null
                || preventa.getTransporte().trim().isEmpty()) {

            throw new RuntimeException(
                    "No se puede generar la cotización: "
                            + "la preventa no tiene transporte registrado."
            );
        }

        if (preventa.getRazonSocial() == null
                || preventa.getRazonSocial().trim().isEmpty()) {

            throw new RuntimeException(
                    "No se puede generar la cotización: "
                            + "la preventa no tiene razón social del cliente."
            );
        }

        if (preventa.getRuc() == null
                || preventa.getRuc().trim().isEmpty()) {

            throw new RuntimeException(
                    "No se puede generar la cotización: "
                            + "la preventa no tiene RUC del cliente."
            );
        }

        if (preventa.getDetalles() == null
                || preventa.getDetalles().isEmpty()) {

            throw new RuntimeException(
                    "No se puede generar la cotización: "
                            + "la preventa no tiene productos registrados."
            );
        }

        for (DetallePreventa detalle : preventa.getDetalles()) {

            if (detalle.getProducto() == null) {
                throw new RuntimeException(
                        "No se puede generar la cotización: "
                                + "uno de los detalles no tiene producto."
                );
            }

            if (detalle.getCantidad() == null
                    || detalle.getCantidad() <= 0) {

                throw new RuntimeException(
                        "No se puede generar la cotización: "
                                + "uno de los productos tiene una cantidad inválida."
                );
            }

            if (detalle.getPrecioUnitarioUsd() == null
                    || detalle.getPrecioUnitarioUsd() < 0) {

                throw new RuntimeException(
                        "No se puede generar la cotización: "
                                + "uno de los productos tiene un precio inválido."
                );
            }

            if (detalle.getTotalUsd() == null
                    || detalle.getTotalUsd() < 0) {

                throw new RuntimeException(
                        "No se puede generar la cotización: "
                                + "uno de los productos tiene un importe inválido."
                );
            }
        }

        // Validar totales de la preventa
        if (preventa.getSubtotalUsd() == null
                || preventa.getSubtotalUsd() < 0) {

            throw new RuntimeException(
                    "No se puede generar la cotización: "
                            + "el subtotal de la preventa no es válido."
            );
        }

        if (preventa.getIgvUsd() == null
                || preventa.getIgvUsd() < 0) {

            throw new RuntimeException(
                    "No se puede generar la cotización: "
                            + "el IGV de la preventa no es válido."
            );
        }

        if (preventa.getTotalUsd() == null
                || preventa.getTotalUsd() < 0) {

            throw new RuntimeException(
                    "No se puede generar la cotización: "
                            + "el total de la preventa no es válido."
            );
        }

        
        // CREAR COTIZACIÓN
        

        Cotizacion cotizacion = new Cotizacion();

        String ultimoNumero =
                cotizacionRepository.obtenerUltimoNumero();

        int siguienteNumero = 1;

        if (ultimoNumero != null
                && ultimoNumero.startsWith("COT.")) {

            String numeroTexto =
                    ultimoNumero.substring(4);

            siguienteNumero =
                    Integer.parseInt(numeroTexto) + 1;
        }

        cotizacion.setNumero(
                String.format("COT.%03d", siguienteNumero)
        );

        cotizacion.setFecha(LocalDate.now());

        
        cotizacion.setFormaPago(
                request.getFormaPago()
        );

        cotizacion.setPreventa(
                preventa
        );

        cotizacion.setSubtotalUsd(
                preventa.getSubtotalUsd()
        );

        cotizacion.setIgvUsd(
                preventa.getIgvUsd()
        );

        cotizacion.setTotalUsd(
                preventa.getTotalUsd()
        );

        // Guardar cotizacion
        Cotizacion cotizacionGuardada =
                cotizacionRepository.save(cotizacion);

        
        // COPIAR DETALLES DE LA PREVENTA
        

        for (DetallePreventa detallePreventa
                : preventa.getDetalles()) {

            DetalleCotizacion detalleCotizacion =
                    new DetalleCotizacion();

            detalleCotizacion.setCotizacion(
                    cotizacionGuardada
            );

            Producto producto =
                    detallePreventa.getProducto();

            detalleCotizacion.setProducto(
                    producto
            );

            detalleCotizacion.setCantidad(
                    detallePreventa.getCantidad()
            );

            detalleCotizacion.setPrecioUnitarioUsd(
                    detallePreventa.getPrecioUnitarioUsd()
            );

            detalleCotizacion.setTotalUsd(
                    detallePreventa.getTotalUsd()
            );

            detalleCotizacionRepository.save(
                    detalleCotizacion
            );
        }

        return cotizacionGuardada;
    }

    public List<Cotizacion> listarCotizaciones(
                String usuario,
                String rol
        ) {

        List<Cotizacion> cotizaciones =
                cotizacionRepository.findAll();

        if ("ADMIN".equalsIgnoreCase(rol)) {
                return cotizaciones;
        }

        return cotizaciones.stream()
                .filter(cotizacion ->
                        cotizacion.getPreventa() != null
                                && usuario.equalsIgnoreCase(
                                        cotizacion.getPreventa().getVendedor()
                                )
                )
                .toList();
        }

    public Cotizacion obtenerCotizacion(Long id) {

        return cotizacionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No se encontró la cotización con ID: "
                                        + id
                        )
                );
    }
}