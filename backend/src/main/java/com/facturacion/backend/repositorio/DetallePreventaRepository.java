package com.facturacion.backend.repositorio;

import com.facturacion.backend.entidad.DetallePreventa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetallePreventaRepository extends JpaRepository<DetallePreventa, Long> {
}