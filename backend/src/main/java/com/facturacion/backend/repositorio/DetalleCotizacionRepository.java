package com.facturacion.backend.repositorio;

import com.facturacion.backend.entidad.DetalleCotizacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleCotizacionRepository extends JpaRepository<DetalleCotizacion, Long> {
}
