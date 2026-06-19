package integrador.prog2;

import integrador.prog2.entities.Usuario;
import integrador.prog2.entities.Producto;
import integrador.prog2.entities.Pedido;
import integrador.prog2.entities.DetallePedido;
import integrador.prog2.entities.Categoria;
import integrador.prog2.enums.Rol;
import integrador.prog2.enums.Estado;

public class main {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("🚀 ¡Probando el Sistema de Pedidos!");
        System.out.println("=========================================");

        // 1. Creamos un cliente
        Usuario cliente = new Usuario();
        cliente.setNombre("Facundo");
        cliente.setApellido("Deseff");
        cliente.setRol(Rol.USUARIO);

        // 2. Creamos una categoría y un producto
        Categoria catHamburguesas = new Categoria();
        catHamburguesas.setNombre("Hamburguesas");


        Producto burger = new Producto();
        burger.setNombre("Hamburguesa Completa");
        burger.setPrecio(6500.0);
        burger.setCategoria(catHamburguesas);

        Producto gaseosa = new Producto();
        gaseosa.setNombre("Coca Cola 500ml");
        gaseosa.setPrecio(1800.0);

        // 3. Iniciamos un pedido para el cliente
        Pedido pedidoFacu = new Pedido();
        pedidoFacu.setUsuario(cliente);
        pedidoFacu.setEstado(Estado.PENDIENTE);

        // 4. El cliente compra: 2 burgers y 1 coca
        DetallePedido renglon1 = new DetallePedido(2, burger.getPrecio(), burger, pedidoFacu);
        DetallePedido renglon2 = new DetallePedido(1, gaseosa.getPrecio(), gaseosa, pedidoFacu);

        // 5. Agregamos los renglones al pedido (esto calcula el total automáticamente)
        pedidoFacu.agregarDetalle(renglon1);
        pedidoFacu.agregarDetalle(renglon2);

        // 6. Mostramos los resultados en la consola
        System.out.println("Cliente: " + pedidoFacu.getUsuario().getNombre() + " " + pedidoFacu.getUsuario().getApellido());
        System.out.println("Estado del pedido: " + pedidoFacu.getEstado());
        System.out.println("-----------------------------------------");
        System.out.println("- " + renglon1.getCantidad() + "x " + renglon1.getProducto().getNombre() + " | Subtotal: $" + renglon1.getSubtotal());
        System.out.println("- " + renglon2.getCantidad() + "x " + renglon2.getProducto().getNombre() + " | Subtotal: $" + renglon2.getSubtotal());
        System.out.println("-----------------------------------------");
        System.out.println("💰 TOTAL DEL PEDIDO: $" + pedidoFacu.getTotal());
        System.out.println("=========================================");
    }
}