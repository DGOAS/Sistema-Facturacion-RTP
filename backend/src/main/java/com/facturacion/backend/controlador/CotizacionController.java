package com.facturacion.backend.controlador;

import com.facturacion.backend.dto.CotizacionRequest;
import com.facturacion.backend.entidad.Cotizacion;
import com.facturacion.backend.servicio.CotizacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.facturacion.backend.servicio.PdfCotizacionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/cotizaciones")
@CrossOrigin(origins = "http://localhost:4200")
public class CotizacionController {

    private final CotizacionService cotizacionService;
    private final PdfCotizacionService pdfCotizacionService;

    public CotizacionController(
            CotizacionService cotizacionService,
            PdfCotizacionService pdfCotizacionService) {

        this.cotizacionService = cotizacionService;
        this.pdfCotizacionService = pdfCotizacionService;
}

    @PostMapping
    public ResponseEntity<Cotizacion> registrarCotizacion(
            @RequestBody CotizacionRequest request) {

        Cotizacion cotizacion =
                cotizacionService.registrarCotizacion(request);

        return ResponseEntity.ok(cotizacion);
    }

    @GetMapping
        public ResponseEntity<?> listarCotizaciones(
                @RequestParam String usuario,
                @RequestParam String rol
        ) {
        return ResponseEntity.ok(
                cotizacionService.listarCotizaciones(usuario, rol)
        );
        }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCotizacion(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                cotizacionService.obtenerCotizacion(id)
        );
    }

    @GetMapping("/{id}/pdf")
    






    public ResponseEntity<byte[]> descargarPdf(
            @PathVariable Long id) {

        Cotizacion cotizacion =
                cotizacionService.obtenerCotizacion(id);

        byte[] pdf =
                pdfCotizacionService.generarPdf(cotizacion);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=cotizacion-" +
                        cotizacion.getNumero() +
                        ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}
