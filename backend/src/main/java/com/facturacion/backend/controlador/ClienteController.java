package com.facturacion.backend.controlador;

import com.facturacion.backend.entidad.Cliente;
import com.facturacion.backend.servicio.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {

        return clienteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<Cliente> buscarPorRuc(@PathVariable String ruc) {

        return clienteService.buscarPorRuc(ruc)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscarPorRazonSocial(
            @RequestParam String razonSocial) {

        return ResponseEntity.ok(
                clienteService.buscarPorRazonSocial(razonSocial)
        );
    }

    @PostMapping
    public ResponseEntity<Cliente> guardarCliente(
            @RequestBody Cliente cliente) {

        return ResponseEntity.ok(
                clienteService.guardarCliente(cliente)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(
            @PathVariable Long id,
            @RequestBody Cliente datos) {

        return clienteService.actualizarCliente(id, datos)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarCliente(
            @PathVariable Long id) {

        boolean desactivado = clienteService.desactivarCliente(id);

        if (!desactivado) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}