package com.facturacion.backend.servicio;

import com.facturacion.backend.dto.DetallePreventaRequest;
import com.facturacion.backend.dto.PreventaConsultaResponse;
import com.facturacion.backend.dto.PreventaRequest;
import com.facturacion.backend.entidad.Cliente;
import com.facturacion.backend.entidad.DetallePreventa;
import com.facturacion.backend.entidad.Producto;
import com.facturacion.backend.entidad.Preventa;
import com.facturacion.backend.repositorio.ClienteRepository;
import com.facturacion.backend.repositorio.CotizacionRepository;
import com.facturacion.backend.repositorio.DetallePreventaRepository;
import com.facturacion.backend.repositorio.ProductoRepository;
import com.facturacion.backend.repositorio.PreventaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PreventaService {

    private final PreventaRepository preventaRepository;
    private final ProductoRepository productoRepository;
    private final DetallePreventaRepository detallePreventaRepository;
    private final ClienteRepository clienteRepository;
    private final CotizacionRepository cotizacionRepository;

    public PreventaService(
            PreventaRepository preventaRepository,
            ProductoRepository productoRepository,
            DetallePreventaRepository detallePreventaRepository,
            ClienteRepository clienteRepository,
            CotizacionRepository cotizacionRepository
    ) {
        this.preventaRepository = preventaRepository;
        this.productoRepository = productoRepository;
        this.detallePreventaRepository = detallePreventaRepository;
        this.clienteRepository = clienteRepository;
        this.cotizacionRepository = cotizacionRepository;
    }

    @Transactional
    public Preventa registrarPreventa(PreventaRequest request) {

        // TEMPORAL: diagnóstico del vendedor recibido
        System.out.println("VENDEDOR RECIBIDO: " + request.getVendedor());

        Preventa preventa = new Preventa();

        String ultimoNumero = preventaRepository.obtenerUltimoNumero();

        int siguienteNumero = 1015;

        if (ultimoNumero != null && ultimoNumero.startsWith("PV.")) {
            String numeroTexto = ultimoNumero.substring(3);
            siguienteNumero = Integer.parseInt(numeroTexto) + 1;
        }

        preventa.setNumero("PV." + siguienteNumero);
        preventa.setFecha(LocalDate.now());

        preventa.setTipoVenta(request.getTipoVenta());
        preventa.setCondicionPago(request.getCondicionPago());
        preventa.setPlazoPago(request.getPlazoPago());

        preventa.setTransporte(request.getTransporte());
        preventa.setAgenciaTransporte(request.getAgenciaTransporte());
        preventa.setAgenciaRuc(request.getAgenciaRuc());
        preventa.setAgenciaTelefono(request.getAgenciaTelefono());

        // Buscar y asociar cliente
        if (request.getIdCliente() != null) {

            Cliente cliente = clienteRepository.findById(request.getIdCliente())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "No se encontró el cliente con ID: "
                                            + request.getIdCliente()
                            )
                    );

            if (!Boolean.TRUE.equals(cliente.getActivo())) {
                throw new RuntimeException(
                        "El cliente seleccionado está desactivado."
                );
            }

            preventa.setCliente(cliente);
        }

        // Datos del cliente
        preventa.setRazonSocial(request.getRazonSocial());
        preventa.setRuc(request.getRuc());
        preventa.setTelefono(request.getTelefono());
        preventa.setDireccion(request.getDireccion());
        preventa.setUrbanizacion(request.getUrbanizacion());
        preventa.setDistrito(request.getDistrito());

        // Representante del cliente
        preventa.setRepresentanteCliente(
                request.getRepresentanteCliente()
        );

        // Vendedor
        preventa.setVendedor(request.getVendedor());

        // Totales
        preventa.setDescuentoPorcentaje(
                request.getDescuentoPorcentaje()
        );

        preventa.setSubtotalUsd(
                request.getSubtotalUsd()
        );

        preventa.setDescuentoUsd(
                request.getDescuentoUsd()
        );

        preventa.setNetoUsd(
                request.getNetoUsd()
        );

        preventa.setIgvUsd(
                request.getIgvUsd()
        );

        preventa.setTotalUsd(
                request.getTotalUsd()
        );

        // Guardar preventa
        Preventa preventaGuardada =
                preventaRepository.save(preventa);

        // Guardar detalles
        for (DetallePreventaRequest detalleRequest
                : request.getDetalles()) {

            Optional<Producto> productoOptional =
                    productoRepository.findByCodigo(
                            detalleRequest.getCodigo()
                    );

            if (productoOptional.isEmpty()) {
                throw new RuntimeException(
                        "No se encontró el producto con código: "
                                + detalleRequest.getCodigo()
                );
            }

            Producto producto = productoOptional.get();

            DetallePreventa detalle =
                    new DetallePreventa();

            detalle.setPreventa(preventaGuardada);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleRequest.getCantidad());
            detalle.setPrecioUnitarioUsd(
                    detalleRequest.getPrecioUnitarioUsd()
            );
            detalle.setTotalUsd(
                    detalleRequest.getTotalUsd()
            );

            detallePreventaRepository.save(detalle);
        }

        return preventaGuardada;
    }

    public List<PreventaConsultaResponse> listarPreventas(
                String usuario,
                String rol
        ) {

            List<Preventa> preventas;

            if ("ADMIN".equalsIgnoreCase(rol)) {
                preventas = preventaRepository.findAll();
            } else {
                preventas = preventaRepository.findByVendedor(usuario);
            }

        return preventas.stream()
                .map(preventa -> {

                    long cantidadCotizaciones =
                            cotizacionRepository.countByPreventa_IdPreventa(
                                    preventa.getIdPreventa()
                            );

                    String estado = cantidadCotizaciones > 0
                            ? "COTIZADA"
                            : "REGISTRADA";

                    return new PreventaConsultaResponse(
                            preventa,
                            estado
                    );
                })
                .toList();
    }

    public Preventa obtenerPreventa(Long id) {

        return preventaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No se encontró la preventa con ID: " + id
                        )
                );
    }
}