package integrador.prog2;

import integrador.prog2.controllers.PedidoController;
import integrador.prog2.entities.DetallePedido;
import integrador.prog2.entities.Pedido;
import integrador.prog2.entities.Producto;

public class main {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("🎛️ Testeando la capa de Controladores...");
        System.out.println("=========================================");

        // 1. Instanciamos el controlador (la mesa de entrada)
        PedidoController pedidoController = new PedidoController();

        // 2. Creamos un pedido VÁLIDO (con un producto de $5000)
        Pedido pedidoOk = new Pedido();
        Producto prod = new Producto();
        prod.setNombre("Super Pancho");
        prod.setPrecio(5000.0);

        DetallePedido detalle = new DetallePedido(1, prod.getPrecio(), prod, pedidoOk);
        pedidoOk.agregarDetalle(detalle);

        // 3. Creamos un pedido TRUCHO (completamente vacío)
        Pedido pedidoTrucho = new Pedido();

        // 4. Se los mandamos al controlador
        System.out.println("--- Enviando pedido correcto al controlador ---");
        pedidoController.registrarPedido(pedidoOk);

        System.out.println("\n--- Enviando pedido vacío al controlador ---");
        pedidoController.registrarPedido(pedidoTrucho);

        System.out.println("=========================================");
        System.out.println("🏁 Fin del test de arquitectura por capas.");
        System.out.println("=========================================");
    }
}