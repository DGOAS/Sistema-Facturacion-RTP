package com.facturacion.backend.repositorio;

import com.facturacion.backend.entidad.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {

    long countByPreventa_IdPreventa(Long idPreventa);

    boolean existsByPreventa_IdPreventa(Long idPreventa);
    
    @Query("""
        SELECT MAX(c.numero)
        FROM Cotizacion c
        WHERE c.numero LIKE 'COT.%'
    """)
    String obtenerUltimoNumero();

    
}
