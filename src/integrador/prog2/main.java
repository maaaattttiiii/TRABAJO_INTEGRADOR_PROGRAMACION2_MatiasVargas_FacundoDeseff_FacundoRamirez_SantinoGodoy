package integrador.prog2;

import integrador.prog2.controllers.PedidoController;
import integrador.prog2.controllers.ProductoController;
import integrador.prog2.controllers.UsuarioController;
import integrador.prog2.entities.Categoria;
import integrador.prog2.entities.Pedido;
import integrador.prog2.entities.Producto;
import integrador.prog2.entities.Usuario;

public class main {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("🕹️ TEST RECONTRAD DEFINITIVO DE CONTROLADORES 🕹️");
        System.out.println("=========================================");

        // 1. Instanciamos los tres controladores (la mesa de entrada completa)
        UsuarioController usuarioController = new UsuarioController();
        ProductoController productoController = new ProductoController();
        PedidoController pedidoController = new PedidoController();

        System.out.println("\n--- 1. Probando Capa de Usuarios ---");
        // Usamos el constructor nuevo que armamos por parámetros
        Usuario clienteOk = new Usuario("Facundo", "Deseff", "facu@gmail.com");
        usuarioController.registrarUsuario(clienteOk);

        System.out.println("\n--- 2. Probando Capa de Productos ---");
        Categoria catGaseosas = new Categoria();
        catGaseosas.setNombre("Gaseosas");

        // Usamos el constructor nuevo de producto
        Producto coca = new Producto("Coca Cola 500ml", 1800.0, catGaseosas);
        productoController.registrarProducto(coca);

        System.out.println("\n--- 3. Probando Flujo Completo de Pedido ---");
        // Armamos el pedido usando la estructura de tu amigo (pide usuario en constructor)
        Pedido pedidoValido = new Pedido(clienteOk);
        pedidoValido.addDetallePedido(2, coca); // Agrega cantidad y producto

        // El controlador de pedidos va a validar todo de forma profunda
        pedidoController.registrarPedido(pedidoValido);

        System.out.println("\n=========================================");
        System.out.println("🏁 Fin del test de integración de capas.");
        System.out.println("=========================================");
    }
}