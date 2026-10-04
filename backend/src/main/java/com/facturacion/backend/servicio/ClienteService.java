package com.facturacion.backend.servicio;

import com.facturacion.backend.entidad.Cliente;
import com.facturacion.backend.repositorio.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findByActivoTrue();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> buscarPorRuc(String ruc) {
        return clienteRepository.findByRuc(ruc);
    }

    public List<Cliente> buscarPorRazonSocial(String razonSocial) {
        return clienteRepository.findByRazonSocialContainingIgnoreCase(razonSocial);
    }

    public Cliente guardarCliente(Cliente cliente) {
        if (cliente.getActivo() == null) {
            cliente.setActivo(true);
        }

        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> actualizarCliente(Long id, Cliente datos) {

        Optional<Cliente> clienteExistente = clienteRepository.findById(id);

        if (clienteExistente.isEmpty()) {
            return Optional.empty();
        }

        Cliente cliente = clienteExistente.get();

        cliente.setRazonSocial(datos.getRazonSocial());
        cliente.setRuc(datos.getRuc());
        cliente.setTelefono(datos.getTelefono());
        cliente.setDireccion(datos.getDireccion());
        cliente.setUrbanizacion(datos.getUrbanizacion());
        cliente.setDistrito(datos.getDistrito());

        return Optional.of(clienteRepository.save(cliente));
    }

    public boolean desactivarCliente(Long id) {

        Optional<Cliente> clienteExistente = clienteRepository.findById(id);

        if (clienteExistente.isEmpty()) {
            return false;
        }

        Cliente cliente = clienteExistente.get();
        cliente.setActivo(false);

        clienteRepository.save(cliente);

        return true;
    }
}