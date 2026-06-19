package integrador.prog2;

import integrador.prog2.controllers.PedidoController;
import integrador.prog2.entities.Categoria;
import integrador.prog2.entities.DetallePedido;
import integrador.prog2.entities.Pedido;
import integrador.prog2.entities.Producto;
import integrador.prog2.entities.Usuario;

public class main {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("🚀 Testeando Validación Interconectada...");
        System.out.println("=========================================");

        PedidoController pedidoController = new PedidoController();

        // 1. Creamos un usuario válido con todos sus campos
        Usuario clienteOk = new Usuario();
        clienteOk.setNombre("Facundo");
        clienteOk.setApellido("Deseff");
        clienteOk.setMail("facu@gmail.com");

        // 2. Creamos una categoría y producto válidos
        Categoria catGaseosas = new Categoria();
        catGaseosas.setNombre("Gaseosas");

        Producto coca = new Producto();
        coca.setNombre("Coca Cola 500ml");
        coca.setPrecio(1800.0);
        coca.setCategoria(catGaseosas);

        // 3. Armamos el pedido correcto
        Pedido pedidoValido = new Pedido();
        pedidoValido.setUsuario(clienteOk); // Tiene usuario ok

        DetallePedido detalleOk = new DetallePedido(2, coca.getPrecio(), coca, pedidoValido);
        pedidoValido.agregarDetalle(detalleOk); // Tiene productos ok

        // 4. Se lo mandamos al controlador
        System.out.println("--- Ejecutando pedido con datos 100% válidos ---");
        pedidoController.registrarPedido(pedidoValido);

        System.out.println("=========================================");
    }
}