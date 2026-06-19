package integrador.prog2;

import integrador.prog2.entities.Pedido;
import integrador.prog2.entities.Usuario;
import integrador.prog2.enums.Rol;
import integrador.prog2.exception.ValidacionNegocioException;
import integrador.prog2.services.PedidoService;

public class main {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("🧪 Probando Validaciones del Servicio...");
        System.out.println("=========================================");

        // 1. Instanciamos el servicio y un pedido vacío adrede
        PedidoService pedidoService = new PedidoService();
        Pedido pedidoVacio = new Pedido();

        Usuario cliente = new Usuario();
        cliente.setNombre("Facu");
        cliente.setRol(Rol.USUARIO);
        pedidoVacio.setUsuario(cliente);

        // 2. Intentamos procesar el pedido vacío usando un bloque try-catch
        try {

            pedidoService.procesarPedido(pedidoVacio);

        } catch (ValidacionNegocioException e) {
            // Si el servicio detecta el error, frena el programa acá y nos muestra su mensaje
            System.out.println("⚠️ ¡Excepción capturada con éxito en el main!");
            System.out.println("Mensaje del error: " + e.getMessage());
        }

        System.out.println("=========================================");
        System.out.println("🏁 El programa continuó corriendo sin romperse gracias al try-catch.");
        System.out.println("=========================================");
    }
}