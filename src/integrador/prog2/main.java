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
        System.out.println("----------------------------------------");
        System.out.println("[TEST] Verificacion de Capas y Controladores");
        System.out.println("----------------------------------------");

        UsuarioController usuarioController = new UsuarioController();
        ProductoController productoController = new ProductoController();
        PedidoController pedidoController = new PedidoController();

        System.out.println("\n--- Test Capa de Usuarios ---");
        Usuario clienteOk = new Usuario("Facundo", "Deseff", "facu@gmail.com");
        usuarioController.registrarUsuario(clienteOk);

        System.out.println("\n--- Test Capa de Productos ---");
        Categoria catGaseosas = new Categoria();
        catGaseosas.setNombre("Gaseosas");

        Producto coca = new Producto("Coca Cola 500ml", 1800.0, catGaseosas);
        productoController.registrarProducto(coca);

        System.out.println("\n--- Test Flujo Completo de Pedido ---");
        Pedido pedidoValido = new Pedido(clienteOk);
        pedidoValido.addDetallePedido(2, coca);

        pedidoController.registrarPedido(pedidoValido);

        System.out.println("\n----------------------------------------");
        System.out.println("[OK] Pruebas finalizadas correctamente.");
        System.out.println("----------------------------------------");
    }
}