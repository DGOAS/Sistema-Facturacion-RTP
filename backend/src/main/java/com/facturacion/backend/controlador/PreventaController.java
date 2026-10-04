package com.facturacion.backend.controlador;

import com.facturacion.backend.dto.PreventaRequest;
import com.facturacion.backend.entidad.Preventa;
import com.facturacion.backend.servicio.PdfPreventaService;
import com.facturacion.backend.servicio.PreventaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/preventas")
@CrossOrigin(origins = "http://localhost:4200")
public class PreventaController {

        private final PreventaService preventaService;
    private final PdfPreventaService pdfPreventaService;

    public PreventaController(
            PreventaService preventaService,
            PdfPreventaService pdfPreventaService) {

        this.preventaService = preventaService;
        this.pdfPreventaService = pdfPreventaService;
    }

    @PostMapping
    public ResponseEntity<Preventa> registrarPreventa(
            @RequestBody PreventaRequest request) {

        Preventa preventa = preventaService.registrarPreventa(request);

        return ResponseEntity.ok(preventa);
    }

    @GetMapping
    public ResponseEntity<?> listarPreventas(
            @RequestParam String usuario,
            @RequestParam String rol
    ) {
        return ResponseEntity.ok(
                preventaService.listarPreventas(usuario, rol)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPreventa(@PathVariable Long id) {
        return ResponseEntity.ok(preventaService.obtenerPreventa(id));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Long id) {

        Preventa preventa = preventaService.obtenerPreventa(id);

        byte[] pdf = pdfPreventaService.generarPdf(preventa);

        return ResponseEntity.ok()
                .header(
                        org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=preventa-" +
                        preventa.getNumero() +
                        ".pdf"
                )
                .contentType(
                        org.springframework.http.MediaType.APPLICATION_PDF
                )
                .body(pdf);
    }

}