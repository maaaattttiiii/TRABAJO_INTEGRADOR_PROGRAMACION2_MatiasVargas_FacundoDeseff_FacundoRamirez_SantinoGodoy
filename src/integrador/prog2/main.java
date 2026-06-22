package integrador.prog2;

import integrador.prog2.controllers.CategoriaController;
import integrador.prog2.controllers.PedidoController;
import integrador.prog2.controllers.ProductoController;
import integrador.prog2.controllers.UsuarioController;
import integrador.prog2.entities.Categoria;
import integrador.prog2.entities.Pedido;
import integrador.prog2.entities.Producto;
import integrador.prog2.entities.Usuario;
import integrador.prog2.enums.Estado;
import integrador.prog2.enums.FormaPago;
import integrador.prog2.enums.Rol;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class main {

    private static Scanner scanner = new Scanner(System.in);
    private static CategoriaController categoriaController = new CategoriaController();
    private static ProductoController productoController = new ProductoController();
    private static UsuarioController usuarioController = new UsuarioController();
    private static PedidoController pedidoController = new PedidoController();

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("\n========================================");
            System.out.println("   SISTEMA DE PEDIDOS (FOOD STORE)");
            System.out.println("========================================");
            System.out.println("1. Categorias");
            System.out.println("2. Productos");
            System.out.println("3. Usuarios");
            System.out.println("4. Pedidos");
            System.out.println("0. Salir");
            System.out.print("Seleccione: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> menuCategorias();
                case 2 -> menuProductos();
                case 3 -> menuUsuarios();
                case 4 -> menuPedidos();
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    // ==================== CATEGORIAS ====================
    private static void menuCategorias() {
        int opcion;
        do {
            System.out.println("\n--- CATEGORIAS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> listarCategorias();
                case 2 -> crearCategoria();
                case 3 -> editarCategoria();
                case 4 -> eliminarCategoria();
                case 0 -> {}
                default -> System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    private static void listarCategorias() {
        List<Categoria> categorias = categoriaController.listarCategorias();
        if (categorias.isEmpty()) {
            System.out.println("No hay categorias cargadas.");
            return;
        }
        System.out.printf("\n%-5s %-20s %-30s%n", "ID", "Nombre", "Descripcion");
        System.out.println("-".repeat(55));
        for (Categoria c : categorias) {
            System.out.printf("%-5d %-20s %-30s%n", c.getId(), c.getNombre(), c.getDescripcion());
        }
    }

    private static void crearCategoria() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Descripcion: ");
        String descripcion = scanner.nextLine().trim();

        Categoria cat = new Categoria();
        cat.setNombre(nombre);
        cat.setDescripcion(descripcion);
        categoriaController.registrarCategoria(cat);
    }

    private static void editarCategoria() {
        listarCategorias();
        System.out.print("ID de la categoria a editar: ");
        Long id = leerLong();
        Optional<Categoria> opt = categoriaController.buscarPorId(id);
        if (opt.isEmpty()) {
            System.out.println("Categoria no encontrada.");
            return;
        }
        Categoria cat = opt.get();
        System.out.println("Editando: " + cat.getNombre());
        System.out.print("Nuevo nombre (" + cat.getNombre() + "): ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Nueva descripcion (" + cat.getDescripcion() + "): ");
        String desc = scanner.nextLine().trim();

        if (!nombre.isEmpty()) cat.setNombre(nombre);
        if (!desc.isEmpty()) cat.setDescripcion(desc);
        categoriaController.actualizarCategoria(cat);
    }

    private static void eliminarCategoria() {
        listarCategorias();
        System.out.print("ID de la categoria a eliminar: ");
        Long id = leerLong();
        System.out.print("Confirmar (S/N): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("S")) {
            categoriaController.eliminarCategoria(id);
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    // ==================== PRODUCTOS ====================
    private static void menuProductos() {
        int opcion;
        do {
            System.out.println("\n--- PRODUCTOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Listar por categoria");
            System.out.println("3. Crear");
            System.out.println("4. Editar");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> listarProductos();
                case 2 -> listarProductosPorCategoria();
                case 3 -> crearProducto();
                case 4 -> editarProducto();
                case 5 -> eliminarProducto();
                case 0 -> {}
                default -> System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    private static void listarProductos() {
        List<Producto> productos = productoController.listarProductos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos cargados.");
            return;
        }
        System.out.printf("\n%-5s %-25s %-10s %-8s %-15s%n", "ID", "Nombre", "Precio", "Stock", "Categoria");
        System.out.println("-".repeat(63));
        for (Producto p : productos) {
            System.out.printf("%-5d %-25s $%-9.2f %-8d %-15s%n",
                    p.getId(), p.getNombre(), p.getPrecio(), p.getStock(),
                    p.getCategoria() != null ? p.getCategoria().getNombre() : "N/A");
        }
    }

    private static void listarProductosPorCategoria() {
        listarCategorias();
        System.out.print("ID de categoria: ");
        Long idCat = leerLong();
        List<Producto> productos = productoController.listarPorCategoria(idCat);
        if (productos.isEmpty()) {
            System.out.println("No hay productos en esta categoria.");
            return;
        }
        for (Producto p : productos) {
            System.out.printf("[%d] %s - $%.2f (Stock: %d)%n",
                    p.getId(), p.getNombre(), p.getPrecio(), p.getStock());
        }
    }

    private static void crearProducto() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Descripcion: ");
        String desc = scanner.nextLine().trim();
        System.out.print("Precio: ");
        double precio = leerDouble();
        System.out.print("Stock: ");
        int stock = leerEntero();
        System.out.print("URL imagen: ");
        String image = scanner.nextLine().trim();
        System.out.print("Disponible (true/false): ");
        boolean disponible = Boolean.parseBoolean(scanner.nextLine().trim());

        listarCategorias();
        System.out.print("ID de categoria: ");
        Long idCat = leerLong();

        Categoria cat = new Categoria();
        cat.setId(idCat);

        Producto prod = new Producto(nombre, precio, desc, stock, cat);
        prod.setImage(image);
        prod.setDisponible(disponible);
        productoController.registrarProducto(prod);
    }

    private static void editarProducto() {
        listarProductos();
        System.out.print("ID del producto a editar: ");
        Long id = leerLong();
        Optional<Producto> opt = productoController.buscarPorId(id);
        if (opt.isEmpty()) {
            System.out.println("Producto no encontrado.");
            return;
        }
        Producto p = opt.get();
        System.out.println("Editando: " + p.getNombre());

        System.out.print("Nuevo nombre (" + p.getNombre() + "): ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Nueva descripcion (" + p.getDescripcion() + "): ");
        String desc = scanner.nextLine().trim();
        System.out.print("Nuevo precio (" + p.getPrecio() + "): ");
        String precioStr = scanner.nextLine().trim();
        System.out.print("Nuevo stock (" + p.getStock() + "): ");
        String stockStr = scanner.nextLine().trim();

        if (!nombre.isEmpty()) p.setNombre(nombre);
        if (!desc.isEmpty()) p.setDescripcion(desc);
        if (!precioStr.isEmpty()) p.setPrecio(Double.parseDouble(precioStr));
        if (!stockStr.isEmpty()) p.setStock(Integer.parseInt(stockStr));

        productoController.actualizarProducto(p);
    }

    private static void eliminarProducto() {
        listarProductos();
        System.out.print("ID del producto a eliminar: ");
        Long id = leerLong();
        System.out.print("Confirmar (S/N): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("S")) {
            productoController.eliminarProducto(id);
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    // ==================== USUARIOS ====================
    private static void menuUsuarios() {
        int opcion;
        do {
            System.out.println("\n--- USUARIOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> listarUsuarios();
                case 2 -> crearUsuario();
                case 3 -> editarUsuario();
                case 4 -> eliminarUsuario();
                case 0 -> {}
                default -> System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    private static void listarUsuarios() {
        List<Usuario> usuarios = usuarioController.listarUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios cargados.");
            return;
        }
        System.out.printf("\n%-5s %-15s %-15s %-25s %-10s%n", "ID", "Nombre", "Apellido", "Mail", "Rol");
        System.out.println("-".repeat(70));
        for (Usuario u : usuarios) {
            System.out.printf("%-5d %-15s %-15s %-25s %-10s%n",
                    u.getId(), u.getNombre(), u.getApellido(), u.getMail(), u.getRol());
        }
    }

    private static void crearUsuario() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine().trim();
        System.out.print("Mail: ");
        String mail = scanner.nextLine().trim();
        System.out.print("Celular: ");
        String celular = scanner.nextLine().trim();
        System.out.print("Contrasena: ");
        String pass = scanner.nextLine().trim();
        System.out.print("Rol (ADMIN/USUARIO): ");
        String rolStr = scanner.nextLine().trim().toUpperCase();

        Rol rol;
        try {
            rol = Rol.valueOf(rolStr);
        } catch (IllegalArgumentException e) {
            rol = Rol.USUARIO;
        }

        Usuario u = new Usuario(nombre, apellido, mail);
        u.setCelular(celular);
        u.setContrasenia(pass);
        u.setRol(rol);
        usuarioController.registrarUsuario(u);
    }

    private static void editarUsuario() {
        listarUsuarios();
        System.out.print("ID del usuario a editar: ");
        Long id = leerLong();
        Optional<Usuario> opt = usuarioController.buscarPorId(id);
        if (opt.isEmpty()) {
            System.out.println("Usuario no encontrado.");
            return;
        }
        Usuario u = opt.get();
        System.out.println("Editando: " + u.getNombre() + " " + u.getApellido());

        System.out.print("Nuevo nombre (" + u.getNombre() + "): ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Nuevo apellido (" + u.getApellido() + "): ");
        String apellido = scanner.nextLine().trim();
        System.out.print("Nuevo mail (" + u.getMail() + "): ");
        String mail = scanner.nextLine().trim();
        System.out.print("Nuevo celular (" + u.getCelular() + "): ");
        String celular = scanner.nextLine().trim();

        if (!nombre.isEmpty()) u.setNombre(nombre);
        if (!apellido.isEmpty()) u.setApellido(apellido);
        if (!mail.isEmpty()) u.setMail(mail);
        if (!celular.isEmpty()) u.setCelular(celular);

        usuarioController.actualizarUsuario(u);
    }

    private static void eliminarUsuario() {
        listarUsuarios();
        System.out.print("ID del usuario a eliminar: ");
        Long id = leerLong();
        System.out.print("Confirmar (S/N): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("S")) {
            usuarioController.eliminarUsuario(id);
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    // ==================== PEDIDOS ====================
    private static void menuPedidos() {
        int opcion;
        do {
            System.out.println("\n--- PEDIDOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Listar por usuario");
            System.out.println("3. Crear");
            System.out.println("4. Actualizar estado");
            System.out.println("5. Actualizar forma de pago");
            System.out.println("6. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> listarPedidos();
                case 2 -> listarPedidosPorUsuario();
                case 3 -> crearPedido();
                case 4 -> actualizarEstadoPedido();
                case 5 -> actualizarFormaPagoPedido();
                case 6 -> eliminarPedido();
                case 0 -> {}
                default -> System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    private static void listarPedidos() {
        List<Pedido> pedidos = pedidoController.listarPedidos();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos cargados.");
            return;
        }
        System.out.printf("\n%-5s %-15s %-12s %-15s %-12s%n", "ID", "Usuario", "Estado", "Forma Pago", "Total");
        System.out.println("-".repeat(59));
        for (Pedido p : pedidos) {
            System.out.printf("%-5d %-15s %-12s %-15s $%-11.2f%n",
                    p.getId(),
                    p.getUsuario() != null ? p.getUsuario().getNombre() : "N/A",
                    p.getEstado(),
                    p.getFormaPago() != null ? p.getFormaPago() : "N/A",
                    p.getTotal());
        }
    }

    private static void listarPedidosPorUsuario() {
        System.out.print("ID del usuario: ");
        Long idUsuario = leerLong();
        List<Pedido> pedidos = pedidoController.listarPorUsuario(idUsuario);
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos para este usuario.");
            return;
        }
        for (Pedido p : pedidos) {
            System.out.printf("[%d] Estado: %s | Total: $%.2f%n", p.getId(), p.getEstado(), p.getTotal());
        }
    }

    private static void crearPedido() {
        System.out.print("ID del usuario: ");
        Long idUsuario = leerLong();
        Optional<Usuario> optUsuario = usuarioController.buscarPorId(idUsuario);
        if (optUsuario.isEmpty()) {
            System.out.println("Usuario no encontrado.");
            return;
        }

        Pedido pedido = new Pedido(optUsuario.get());
        boolean agregando = true;

        while (agregando) {
            listarProductos();
            System.out.print("ID del producto (0 para finalizar): ");
            Long idProd = leerLong();
            if (idProd == 0) {
                agregando = false;
                continue;
            }

            Optional<Producto> optProd = productoController.buscarPorId(idProd);
            if (optProd.isEmpty()) {
                System.out.println("Producto no encontrado.");
                continue;
            }

            System.out.print("Cantidad: ");
            int cantidad = leerEntero();
            if (cantidad <= 0) {
                System.out.println("La cantidad debe ser mayor a 0.");
                continue;
            }

            pedido.addDetallePedido(cantidad, optProd.get());
            System.out.println("Producto agregado. Subtotal parcial: $" + pedido.getTotal());
        }

        if (pedido.getDetalles().isEmpty()) {
            System.out.println("Pedido cancelado: no se agregaron productos.");
            return;
        }

        System.out.println("Total del pedido: $" + pedido.getTotal());
        System.out.print("Forma de pago (1-TARJETA, 2-TRANSFERENCIA, 3-EFECTIVO): ");
        int fp = leerEntero();
        switch (fp) {
            case 1 -> pedido.setFormaPago(FormaPago.TARJETA);
            case 2 -> pedido.setFormaPago(FormaPago.TRANSFERENCIA);
            case 3 -> pedido.setFormaPago(FormaPago.EFECTIVO);
            default -> {
                System.out.println("Opcion invalida, se asigna EFECTIVO.");
                pedido.setFormaPago(FormaPago.EFECTIVO);
            }
        }

        pedidoController.registrarPedido(pedido);
    }

    private static void actualizarEstadoPedido() {
        listarPedidos();
        System.out.print("ID del pedido: ");
        Long id = leerLong();
        System.out.println("Estados: 1-PENDIENTE, 2-CONFIRMADO, 3-TERMINADO, 4-CANCELADO");
        System.out.print("Nuevo estado: ");
        int est = leerEntero();
        Estado estado;
        switch (est) {
            case 1 -> estado = Estado.PENDIENTE;
            case 2 -> estado = Estado.CONFIRMADO;
            case 3 -> estado = Estado.TERMINADO;
            case 4 -> estado = Estado.CANCELADO;
            default -> {
                System.out.println("Opcion invalida.");
                return;
            }
        }
        pedidoController.actualizarEstado(id, estado);
    }

    private static void actualizarFormaPagoPedido() {
        listarPedidos();
        System.out.print("ID del pedido: ");
        Long id = leerLong();
        System.out.println("Formas de pago: 1-TARJETA, 2-TRANSFERENCIA, 3-EFECTIVO");
        System.out.print("Nueva forma de pago: ");
        int fp = leerEntero();
        FormaPago forma;
        switch (fp) {
            case 1 -> forma = FormaPago.TARJETA;
            case 2 -> forma = FormaPago.TRANSFERENCIA;
            case 3 -> forma = FormaPago.EFECTIVO;
            default -> {
                System.out.println("Opcion invalida.");
                return;
            }
        }
        pedidoController.actualizarFormaPago(id, forma);
    }

    private static void eliminarPedido() {
        listarPedidos();
        System.out.print("ID del pedido a eliminar: ");
        Long id = leerLong();
        System.out.print("Confirmar (S/N): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("S")) {
            pedidoController.eliminarPedido(id);
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    // ==================== UTILIDADES ====================
    private static int leerEntero() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static Long leerLong() {
        try {
            return Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    private static double leerDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
