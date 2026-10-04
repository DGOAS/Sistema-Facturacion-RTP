package com.facturacion.backend.controlador;

import com.facturacion.backend.entidad.Usuario;
import com.facturacion.backend.servicio.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    
    // LOGIN
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> datos) {

        String usuario = datos.get("usuario");
        String password = datos.get("password");

        Usuario usuarioAutenticado = usuarioService.autenticar(usuario, password);

        if (usuarioAutenticado == null) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Usuario o contraseña incorrectos");

            return ResponseEntity.status(401).body(respuesta);
        }

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Login correcto");
        respuesta.put("usuario", usuarioAutenticado.getUsuario());
        respuesta.put("rol", usuarioAutenticado.getRol());

        return ResponseEntity.ok(respuesta);
    }

    
    // OBTENER PERFIL
    

    @GetMapping("/usuarios/{usuario}")
    public ResponseEntity<?> obtenerPerfil(@PathVariable String usuario) {

        Usuario usuarioEncontrado = usuarioService.obtenerPorUsuario(usuario);

        if (usuarioEncontrado == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("idUsuario", usuarioEncontrado.getIdUsuario());
        respuesta.put("usuario", usuarioEncontrado.getUsuario());
        respuesta.put("nombres", usuarioEncontrado.getNombres());
        respuesta.put("apellidos", usuarioEncontrado.getApellidos());
        respuesta.put("dni", usuarioEncontrado.getDni());
        respuesta.put("telefono", usuarioEncontrado.getTelefono());
        respuesta.put("rol", usuarioEncontrado.getRol());

        return ResponseEntity.ok(respuesta);
    }

    
    // ACTUALIZAR PERFIL
    

    @PutMapping("/usuarios/{usuario}")
    public ResponseEntity<?> actualizarPerfil(
            @PathVariable String usuario,
            @RequestBody Usuario datos) {

        Usuario usuarioActualizado =
                usuarioService.actualizarPerfil(usuario, datos);

        if (usuarioActualizado == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("mensaje", "Perfil actualizado correctamente");
        respuesta.put("usuario", usuarioActualizado.getUsuario());
        respuesta.put("nombres", usuarioActualizado.getNombres());
        respuesta.put("apellidos", usuarioActualizado.getApellidos());
        respuesta.put("dni", usuarioActualizado.getDni());
        respuesta.put("telefono", usuarioActualizado.getTelefono());
        respuesta.put("rol", usuarioActualizado.getRol());

        return ResponseEntity.ok(respuesta);
    }

    
    // CAMBIAR CONTRASEÑA
    

    @PutMapping("/usuarios/{usuario}/password")
    public ResponseEntity<?> cambiarPassword(
            @PathVariable String usuario,
            @RequestBody Map<String, String> datos) {

        String nuevaPassword = datos.get("nuevaPassword");

        if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) {

            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "La nueva contraseña es obligatoria");

            return ResponseEntity.badRequest().body(respuesta);
        }

        boolean actualizado =
                usuarioService.cambiarPassword(usuario, nuevaPassword);

        if (!actualizado) {
            return ResponseEntity.notFound().build();
        }

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Contraseña actualizada correctamente");

        return ResponseEntity.ok(respuesta);
    }


    
        // ADMINISTRACIÓN DE USUARIOS
   

    @GetMapping("/usuarios/admin")
    public ResponseEntity<?> listarUsuarios() {

        java.util.List<Usuario> usuarios = usuarioService.listarUsuarios();

        java.util.List<Map<String, Object>> respuesta = new java.util.ArrayList<>();

        for (Usuario usuario : usuarios) {

            Map<String, Object> datos = new HashMap<>();

            datos.put("idUsuario", usuario.getIdUsuario());
            datos.put("usuario", usuario.getUsuario());
            datos.put("nombres", usuario.getNombres());
            datos.put("apellidos", usuario.getApellidos());
            datos.put("dni", usuario.getDni());
            datos.put("telefono", usuario.getTelefono());
            datos.put("rol", usuario.getRol());
            datos.put("activo", usuario.getActivo());

            respuesta.add(datos);
        }

        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/usuarios/admin")
    public ResponseEntity<?> registrarUsuario(
            @RequestBody Usuario datos) {

        Usuario usuarioRegistrado =
                usuarioService.registrarUsuario(datos);

        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("mensaje", "Usuario registrado correctamente");
        respuesta.put("idUsuario", usuarioRegistrado.getIdUsuario());
        respuesta.put("usuario", usuarioRegistrado.getUsuario());

        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/usuarios/admin/{id}")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody Usuario datos) {

        Usuario usuarioActualizado =
                usuarioService.actualizarUsuario(id, datos);

        if (usuarioActualizado == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("mensaje", "Usuario actualizado correctamente");
        respuesta.put("idUsuario", usuarioActualizado.getIdUsuario());
        respuesta.put("usuario", usuarioActualizado.getUsuario());

        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/usuarios/admin/{id}")
    public ResponseEntity<?> inactivarUsuario(
            @PathVariable Long id) {

        boolean actualizado =
                usuarioService.inactivarUsuario(id);

        if (!actualizado) {
            return ResponseEntity.notFound().build();
        }

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put("mensaje", "Usuario inactivado correctamente");

        return ResponseEntity.ok(respuesta);
    }

           
        // ACTIVAR USUARIO
        
        @PutMapping("/usuarios/admin/{id}/activar")
        public ResponseEntity<?> activarUsuario(
                @PathVariable Long id) {

            boolean actualizado =
                    usuarioService.activarUsuario(id);

            if (!actualizado) {
                return ResponseEntity.notFound().build();
            }

            Map<String, String> respuesta = new HashMap<>();

            respuesta.put(
                    "mensaje",
                    "Usuario activado correctamente"
            );

            return ResponseEntity.ok(respuesta);
        }

}