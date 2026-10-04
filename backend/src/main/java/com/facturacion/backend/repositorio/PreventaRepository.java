package com.facturacion.backend.repositorio;

import com.facturacion.backend.entidad.Preventa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PreventaRepository extends JpaRepository<Preventa, Long> {

    @Query("""
        SELECT MAX(p.numero)
        FROM Preventa p
        WHERE p.numero LIKE 'PV.%'
    """)
    String obtenerUltimoNumero();

    List<Preventa> findByVendedor(String vendedor);
}