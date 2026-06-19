package integrador.prog2;

import integrador.prog2.entities.Usuario;
import integrador.prog2.exception.ValidacionNegocioException;
import integrador.prog2.services.UsuarioService;

public class main {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("🧪 Testeando el Servicio de Usuarios...");
        System.out.println("=========================================");

        UsuarioService usuarioService = new UsuarioService();

        // 1. Creamos un usuario VÁLIDO
        Usuario userOk = new Usuario();
        userOk.setNombre("Facundo");
        userOk.setApellido("Deseff");
        userOk.setMail("facu@gmail.com");

        // 2. Creamos un usuario INVÁLIDO (Mail sin @)
        Usuario userTrucho = new Usuario();
        userTrucho.setNombre("Juan");
        userTrucho.setApellido("Perez");
        userTrucho.setMail("juanperez.com"); // 🔴 Rompe la regla del @

        // 3. Probamos validar ambos en el try-catch
        try {
            // El primero pasa limpio
            usuarioService.validarUsuario(userOk);

            System.out.println("-----------------------------------------");
            System.out.println("⚠️ Ahora intentamos pasar el usuario con mail trucho...");

            // El segundo hace saltar la excepción
            usuarioService.validarUsuario(userTrucho);

        } catch (ValidacionNegocioException e) {
            System.out.println("\n⚠️ ¡Excepción capturada en el main con éxito!");
            System.out.println("Mensaje de error: " + e.getMessage());
        }

        System.out.println("=========================================");
        System.out.println("🏁 Fin del test de usuarios.");
        System.out.println("=========================================");
    }
}