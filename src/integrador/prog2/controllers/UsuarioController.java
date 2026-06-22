package integrador.prog2.controllers;

import integrador.prog2.entities.Usuario;
import integrador.prog2.exception.EntidadNoEncontradaException;
import integrador.prog2.exception.ValidacionNegocioException;
import integrador.prog2.services.UsuarioService;

import java.util.List;
import java.util.Optional;

public class UsuarioController {
    private UsuarioService usuarioService;

    public UsuarioController() {
        this.usuarioService = new UsuarioService();
    }

    public void registrarUsuario(Usuario usuario) {
        try {
            System.out.println("\n[UsuarioController] Peticion recibida para registrar un usuario...");
            usuarioService.guardarUsuario(usuario);
            System.out.println("[UsuarioController] Usuario registrado con exito. ID: " + usuario.getId());
        } catch (ValidacionNegocioException e) {
            System.out.println("[UsuarioController] Error al validar usuario:");
            System.out.println("   -> " + e.getMessage());
        }
    }

    public List<Usuario> listarUsuarios() {
        return usuarioService.listar();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioService.buscarPorId(id);
    }

    public void actualizarUsuario(Usuario usuario) {
        try {
            usuarioService.actualizar(usuario);
            System.out.println("[UsuarioController] Usuario actualizado con exito.");
        } catch (ValidacionNegocioException e) {
            System.out.println("[UsuarioController] Error al actualizar usuario:");
            System.out.println("   -> " + e.getMessage());
        } catch (EntidadNoEncontradaException e) {
            System.out.println("[UsuarioController] " + e.getMessage());
        }
    }

    public void eliminarUsuario(Long id) {
        try {
            usuarioService.eliminar(id);
            System.out.println("[UsuarioController] Usuario eliminado con exito.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("[UsuarioController] " + e.getMessage());
        }
    }
}
