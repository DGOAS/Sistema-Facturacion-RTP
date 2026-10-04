package com.facturacion.backend.servicio;

import com.facturacion.backend.entidad.Usuario;
import com.facturacion.backend.repositorio.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario autenticar(String usuario, String password) {

        
        Usuario usuarioEncontrado = usuarioRepository
                .findByUsuario(usuario)
                .orElse(null);

        if (usuarioEncontrado == null) {
            return null;
        }

        if (!usuarioEncontrado.getActivo()) {
            return null;
        }

        if (!passwordEncoder.matches(password, usuarioEncontrado.getPassword())) {
            return null;
        }

        return usuarioEncontrado;
    }

    public Usuario obtenerPorUsuario(String usuario) {

        return usuarioRepository
                .findByUsuario(usuario)
                .orElse(null);
    }

    public Usuario actualizarPerfil(String usuario, Usuario datos) {

        Usuario usuarioEncontrado = usuarioRepository
                .findByUsuario(usuario)
                .orElse(null);

        if (usuarioEncontrado == null) {
            return null;
        }

        usuarioEncontrado.setNombres(datos.getNombres());
        usuarioEncontrado.setApellidos(datos.getApellidos());
        usuarioEncontrado.setDni(datos.getDni());
        usuarioEncontrado.setTelefono(datos.getTelefono());

        return usuarioRepository.save(usuarioEncontrado);
    }

    public boolean cambiarPassword(String usuario, String nuevaPassword) {

        Usuario usuarioEncontrado = usuarioRepository
                .findByUsuario(usuario)
                .orElse(null);

        if (usuarioEncontrado == null) {
            return false;
        }

        usuarioEncontrado.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuarioEncontrado);

        return true;
    }

    public java.util.List<Usuario> listarUsuarios() {
            return usuarioRepository.findAll();
        }

        public Usuario registrarUsuario(Usuario usuario) {
            usuario.setActivo(true);
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            return usuarioRepository.save(usuario);
        }

        public Usuario actualizarUsuario(Long id, Usuario datos) {

            Usuario usuarioEncontrado = usuarioRepository
                    .findById(id)
                    .orElse(null);

            if (usuarioEncontrado == null) {
                return null;
            }

            usuarioEncontrado.setNombres(datos.getNombres());
            usuarioEncontrado.setApellidos(datos.getApellidos());
            usuarioEncontrado.setDni(datos.getDni());
            usuarioEncontrado.setTelefono(datos.getTelefono());
            usuarioEncontrado.setUsuario(datos.getUsuario());
            usuarioEncontrado.setRol(datos.getRol());

            if (datos.getPassword() != null && !datos.getPassword().trim().isEmpty()) {
            usuarioEncontrado.setPassword(
                passwordEncoder.encode(datos.getPassword())
            );
}

            if (datos.getActivo() != null) {
                usuarioEncontrado.setActivo(datos.getActivo());
            }

            return usuarioRepository.save(usuarioEncontrado);
        }

        public boolean inactivarUsuario(Long id) {

            Usuario usuarioEncontrado = usuarioRepository
                    .findById(id)
                    .orElse(null);

            if (usuarioEncontrado == null) {
                return false;
            }

            usuarioEncontrado.setActivo(false);
            usuarioRepository.save(usuarioEncontrado);

            return true;
        }


        public boolean activarUsuario(Long id) {

            Usuario usuarioEncontrado = usuarioRepository
                    .findById(id)
                    .orElse(null);

            if (usuarioEncontrado == null) {
                return false;
            }

            usuarioEncontrado.setActivo(true);
            usuarioRepository.save(usuarioEncontrado);

            return true;
        }
}