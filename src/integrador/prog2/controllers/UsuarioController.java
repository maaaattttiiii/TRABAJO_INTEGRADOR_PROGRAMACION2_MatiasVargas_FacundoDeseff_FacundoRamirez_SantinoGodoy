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
            System.out.println("\n👤 [UsuarioController] Peticion recibida para registrar un usuario...");
            usuarioService.validarUsuario(usuario);
            System.out.println("👤 [UsuarioController] Usuario validado correctamente.");
        } catch (ValidacionNegocioException e) {
            System.out.println("👤 [UsuarioController] Error al validar usuario:");
            System.out.println("   -> " + e.getMessage());
        }
    }
}