package integrador.prog2.services;

import integrador.prog2.entities.Usuario;
import integrador.prog2.exception.ValidacionNegocioException;

public class UsuarioService {

    // Método para validar que un usuario cumpla con los requisitos del sistema
    public void validarUsuario(Usuario usuario) throws ValidacionNegocioException {
        System.out.println("🔍 Validando datos del usuario: " + usuario.getNombre() + "...");

        // REGLA DE NEGOCIO: Nombre y Apellido obligatorios
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new ValidacionNegocioException("🔴 Error de Negocio: El nombre del usuario es obligatorio.");
        }
        if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
            throw new ValidacionNegocioException("🔴 Error de Negocio: El apellido del usuario es obligatorio.");
        }

        // REGLA DE NEGOCIO: El mail no puede ser vacío y debe contener un '@'
        if (usuario.getMail() == null || usuario.getMail().trim().isEmpty()) {
            throw new ValidacionNegocioException("🔴 Error de Negocio: El correo electrónico es obligatorio.");
        }

        if (!usuario.getMail().contains("@")) {
            throw new ValidacionNegocioException("🔴 Error de Negocio: El correo '" + usuario.getMail() + "' no es un email válido (falta el @).");
        }

        System.out.println("✅ El usuario '" + usuario.getNombre() + " " + usuario.getApellido() + "' es válido y quedó registrado.");
    }
}