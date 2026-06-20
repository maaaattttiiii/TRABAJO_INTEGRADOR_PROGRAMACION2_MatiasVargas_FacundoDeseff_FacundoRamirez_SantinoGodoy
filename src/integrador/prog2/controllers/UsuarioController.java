package integrador.prog2.controllers;

import integrador.prog2.entities.Usuario;
import integrador.prog2.exception.ValidacionNegocioException;
import integrador.prog2.services.UsuarioService;

public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController() {
        this.usuarioService = new UsuarioService();
    }

    public void registrarUsuario(Usuario usuario) {
        try {
            System.out.println("\n🎛️ [Controller] Petición recibida para registrar un usuario...");

            // Delegamos la validación al servicio de usuarios
            usuarioService.validarUsuario(usuario);

            System.out.println("🎛️ [Controller] Usuario validado correctamente.");
        } catch (ValidacionNegocioException e) {
            System.out.println("🎛️ [Controller] Error de negocio al validar usuario:");
            System.out.println("👉 " + e.getMessage());
        }
    }
}